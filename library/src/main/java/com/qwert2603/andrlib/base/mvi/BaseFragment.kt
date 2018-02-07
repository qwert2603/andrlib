package com.qwert2603.andrlib.base.mvi

import android.support.annotation.CallSuper
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.qwert2603.andrlib.util.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>> : MviFragment<V, P>(), BaseView<VS>, FragmentForSnackbar {

    abstract override fun viewForSnackbar(): View?

    private var everRendered = false
    protected var prevViewState: VS? = null
    protected lateinit var currentViewState: VS

    private val viewDisposable = CompositeDisposable()

    @CallSuper
    override fun render(vs: VS) {
        if (everRendered) {
            prevViewState = currentViewState
        } else {
            everRendered = true
        }
        currentViewState = vs
        LogUtils.d("${this.javaClass.simpleName} render ${vs.toString().replace('\n', ' ')}")
    }

    override fun onDestroyView() {
        viewDisposable.clear()
        super.onDestroyView()
    }

    protected fun Disposable.disposeOnDestroyView() {
        viewDisposable.add(this)
    }

    protected fun <T> renderIfChanged(field: VS.() -> T, renderer: (T) -> Unit) {
        val prevViewState = prevViewState
        val currentField = field(currentViewState)
        if (prevViewState == null || currentField !== field(prevViewState)) {
            renderer(currentField)
        }
    }

    protected fun <T, U> renderIfChangedTwo(fields: VS.() -> Pair<T, U>, renderer: (Pair<T, U>) -> Unit) {
        val prevViewState = prevViewState
        val currentField = fields(currentViewState)
        if (prevViewState == null) {
            renderer(currentField)
            return
        }
        val prevField = fields(prevViewState)
        if (currentField.first !== prevField.first || currentField.second !== prevField.second) {
            renderer(currentField)
        }
    }

    protected fun <T, U, V> renderIfChangedThree(fields: VS.() -> Triple<T, U, V>, renderer: (Triple<T, U, V>) -> Unit) {
        val prevViewState = prevViewState
        val currentField = fields(currentViewState)
        if (prevViewState == null) {
            renderer(currentField)
            return
        }
        val prevField = fields(prevViewState)
        if (currentField.first !== prevField.first || currentField.second !== prevField.second || currentField.third !== prevField.third) {
            renderer(currentField)
        }
    }
}