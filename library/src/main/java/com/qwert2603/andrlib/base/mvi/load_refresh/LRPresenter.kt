package com.qwert2603.andrlib.base.mvi.load_refresh

import android.support.annotation.CallSuper
import com.qwert2603.andrlib.base.mvi.BasePresenter
import com.qwert2603.andrlib.base.mvi.PartialChange
import com.qwert2603.andrlib.schedulers.UiSchedulerProvider
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.cancelOn
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

/** Presenter that can load (retry load) and refresh initial model. */
abstract class LRPresenter<A, I, VS : LRViewState, V : LRView<VS>>(uiSchedulerProvider: UiSchedulerProvider)
    : BasePresenter<V, VS>(uiSchedulerProvider) {

    companion object {
        val EMPTY_LR_MODEL = LRModel(false, null, false, false, null)
    }

    protected open val canRefreshAtAll = true

    protected abstract fun initialModelSingle(additionalKey: A): Single<I>

    protected open fun initialModelSingleRefresh(additionalKey: A): Single<I> = initialModelSingle(additionalKey)

    /**
     * @param i initial model from [LRPartialChange.InitialModelLoaded].
     * @return new view state with changed initial model.
     */
    protected abstract fun VS.applyInitialModel(i: I): VS

    /**
     * Classes-inheritors may override it to trigger reload intent when needed.
     * Reload means that initial model will be reloaded.
     */
    protected open val reloadIntent: Observable<Any> = Observable.never()

    /** Load initial model. */
    protected val loadIntent: Observable<Any> = intent { it.load() }.share()

    /** Reload initial model after error loading it. */
    protected val retryIntent: Observable<Any> = intent { it.retry() }.share()

    /**
     * Refresh initial model after it was loaded successfully.
     * While refreshing already loaded initial model will be showing.
     */
    protected open val refreshIntent: Observable<Any> = intent { it.refresh() }.share()

    /** @return Observable that emits every time when loading initial model. */
    protected val initialModelLoading: Observable<Any> by lazy {
        Observable.merge(
                Observable.combineLatest(
                        loadIntent,
                        reloadIntent.startWith(Any()),
                        BiFunction { a, _ -> a }
                ),
                retryIntent,
                refreshIntent
        ).share()
    }

    /** [additionalKey] additional key that can be used for loading initial model. */
    @Suppress("UNCHECKED_CAST")
    protected fun loadRefreshPartialChanges(additionalKey: Observable<A> = Observable.just(Any() as A)): Observable<PartialChange> = Observable.merge(
            Observable
                    .merge(
                            Observable.combineLatest(
                                    loadIntent,
                                    reloadIntent.startWith(Any()),
                                    BiFunction { k, _ -> k }
                            ),
                            retryIntent
                    )
                    .withLatestFrom(additionalKey, BiFunction { _: Any, a: A -> a })
                    .switchMap { a ->
                        initialModelSingle(a)
                                .toObservable()
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.LoadingError(it) }
                                .startWith(LRPartialChange.LoadingStarted())
                    },
            refreshIntent
                    .withLatestFrom(additionalKey, BiFunction { _: Any, a: A -> a })
                    .switchMap { a ->
                        initialModelSingleRefresh(a)
                                .toObservable()
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn {
                                    viewActions.onNext(LRViewAction.RefreshingError(it))
                                    LRPartialChange.RefreshError(it)
                                }
                                .startWith(LRPartialChange.RefreshStarted())
                                .cancelOn(Observable.merge(reloadIntent, retryIntent), LRPartialChange.RefreshCancelled())
                    }
    )

    @CallSuper
    override fun stateReducer(vs: VS, change: PartialChange): VS {
        LogUtils.d { "LRPresenter ${this.javaClass.simpleName} stateReducer ${change.toString().replace('\n', ' ')}" }
        if (change !is LRPartialChange) throw Exception()
        return when (change) {
            is LRPartialChange.LoadingStarted -> vs.changeLRModel(vs.lrModel.copy(loading = true, loadingError = null, canRefresh = false))
            is LRPartialChange.LoadingError -> vs.changeLRModel(vs.lrModel.copy(loading = false, loadingError = change.t))
            is LRPartialChange.RefreshStarted -> vs.changeLRModel(vs.lrModel.copy(refreshing = true, refreshingError = null))
            is LRPartialChange.RefreshError -> vs.changeLRModel(vs.lrModel.copy(refreshing = false, refreshingError = change.t))
            is LRPartialChange.RefreshCancelled -> vs.changeLRModel(vs.lrModel.copy(refreshing = false))
            is LRPartialChange.InitialModelLoaded<*> -> {
                @Suppress("UNCHECKED_CAST")
                vs
                        .changeLRModel(vs.lrModel.copy(
                                loading = false,
                                loadingError = null,
                                canRefresh = canRefreshAtAll,
                                refreshing = false,
                                refreshingError = null
                        ))
                        .applyInitialModel(change.i as I)
            }
        }
    }
}