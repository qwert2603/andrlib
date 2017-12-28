package com.qwert2603.andrlib.base.mvi

import android.support.annotation.CallSuper
import com.qwert2603.andrlib.schedulers.UiSchedulerProvider
import com.qwert2603.andrlib.schedulers.switchToUiIfNotYet
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.addTo
import com.qwert2603.andrlib.util.pausable
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class BasePresenter<V : BaseView<VS>, VS>(protected val uiSchedulerProvider: UiSchedulerProvider)
    : MviBasePresenter<V, VS>() {

    private val viewAttached = PublishSubject.create<Boolean>()

    protected val viewActions: PublishSubject<ViewAction> = PublishSubject.create<ViewAction>()
    private val actionsRelay = viewActions
            .doOnNext { LogUtils.d("viewActions doOnNext $it") }
            .pausable(viewAttached)
    private val actionsObservable: PublishSubject<ViewAction> = PublishSubject.create<ViewAction>()

    private var relayDisposable: Disposable? = null
    private val actionsDisposable = CompositeDisposable()

    private val disposableView = CompositeDisposable()

    override fun attachView(view: V) {
        LogUtils.d("attachView ${hashCode()} $javaClass $view")
        super.attachView(view)
        actionsObservable
                .switchToUiIfNotYet(uiSchedulerProvider)
                .subscribe(view::executeAction)
                .addTo(actionsDisposable)
        if (relayDisposable == null) {
            relayDisposable = actionsRelay.subscribe { actionsObservable.onNext(it) }
        }
        viewAttached.onNext(true)
    }

    override fun detachView() {
        LogUtils.d("BasePresenter#detachView ${hashCode()} $javaClass")
        viewAttached.onNext(false)
        actionsDisposable.clear()
        super.detachView()
    }

    override fun destroy() {
        LogUtils.d("BasePresenter#destroy ${hashCode()} $javaClass")
        actionsDisposable.dispose()
        relayDisposable?.dispose()
        disposableView.dispose()
        super.destroy()
    }

    protected fun <T> Observable<T>.subscribeToView() = this
            .subscribe()
            .addTo(disposableView)

    abstract protected val partialChanges: Observable<PartialChange>
    abstract protected val initialState: VS
    abstract protected fun stateReducer(vs: VS, change: PartialChange): VS

    @CallSuper
    override fun bindIntents() {
        subscribeViewState(
                partialChanges
                        .scan(initialState, this::stateReducer)
                        .skip(1)
                        .switchToUiIfNotYet(uiSchedulerProvider),
                { view, viewState -> view.render(viewState) }
        )
    }
}