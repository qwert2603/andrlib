package com.qwert2603.andrlib.base.mvi.load_refresh

import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import com.qwert2603.andrlib.R
import com.qwert2603.andrlib.base.mvi.BaseFragment
import com.qwert2603.andrlib.base.mvi.BasePresenter
import com.qwert2603.andrlib.base.mvi.ViewAction
import com.qwert2603.andrlib.util.LogUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class LRFragment<VS : LRViewState, V : LRView<VS>, P : BasePresenter<V, VS>> : BaseFragment<VS, V, P>(), LRView<VS> {

    protected abstract fun loadRefreshPanel(): LoadRefreshPanel

    protected val retryRefreshSubject: PublishSubject<Any> = PublishSubject.create<Any>()

    override fun load(): Observable<Any> = Observable.just(Any())

    override fun retry(): Observable<Any> = loadRefreshPanel().retryClicks()

    override fun refresh(): Observable<Any> = Observable.merge(
            loadRefreshPanel().refreshes(),
            retryRefreshSubject
    )

    @CallSuper
    override fun render(vs: VS) {
        super.render(vs)
        vs.lrModel.loadingError?.let { LogUtils.e("loadingError", it) }
        loadRefreshPanel().render(vs)
    }

    @CallSuper
    override fun executeAction(va: ViewAction) {
        LogUtils.d("${this.javaClass.simpleName} executeAction $va")
        if (va is LRViewAction) {
            when (va) {
                is LRViewAction.RefreshingError -> {
                    LogUtils.e("refreshError", va.t)
                    Snackbar.make(view!!, R.string.refreshing_error_text, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.retry_loading_text) { retryRefreshSubject.onNext(Any()) }
                            .show()
                }
            }
        }
    }

    protected fun isLoadingFinishedNow() = prevViewState?.lrModel?.isModelLoaded == false
            && currentViewState.lrModel.isModelLoaded

    protected fun isRefreshingFinishedNow() = prevViewState?.lrModel?.refreshing == true
            && !currentViewState.lrModel.refreshing
            && currentViewState.lrModel.refreshingError == null

    protected fun isInitialModelLoadedOrUpdatedNow() = isLoadingFinishedNow() || isRefreshingFinishedNow()
}