package com.qwert2603.andrlib.base.mvi

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.Quadruple
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>> : MviFragment<V, P>(), BaseView<VS>, FragmentForSnackbar {

    abstract override fun viewForSnackbar(): View?

    private var everRendered = false

    protected var prevViewState: VS? = null

    protected lateinit var currentViewState: VS

    private val viewDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        everRendered = false
        prevViewState = null
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        viewDisposable.clear()
        super.onDestroyView()
    }

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

    protected fun Disposable.disposeOnDestroyView() {
        viewDisposable.add(this)
    }

    protected fun renderAll() {
        everRendered = false
        prevViewState = null
        render(currentViewState)
    }

    protected inline fun <T> renderIfChanged(crossinline field: VS.() -> T, crossinline renderer: (T) -> Unit) {
        val prevViewState = prevViewState
        val currentField = field(currentViewState)
        if (prevViewState == null || currentField !== field(prevViewState)) {
            renderer(currentField)
        }
    }

    protected inline fun <T, U> renderIfChangedTwo(crossinline fields: VS.() -> Pair<T, U>, crossinline renderer: (Pair<T, U>) -> Unit) {
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

    protected inline fun <T, U, V> renderIfChangedThree(crossinline fields: VS.() -> Triple<T, U, V>, crossinline renderer: (Triple<T, U, V>) -> Unit) {
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

    protected inline fun <T, U, V, W> renderIfChangedFour(crossinline fields: VS.() -> Quadruple<T, U, V, W>, crossinline renderer: (Quadruple<T, U, V, W>) -> Unit) {
        val prevViewState = prevViewState
        val currentField = fields(currentViewState)
        if (prevViewState == null) {
            renderer(currentField)
            return
        }
        val prevField = fields(prevViewState)
        if (currentField.first !== prevField.first || currentField.second !== prevField.second
                || currentField.third !== prevField.third || currentField.forth !== prevField.forth) {
            renderer(currentField)
        }
    }

    protected inline fun <T> renderIfChangedWithFirstRendering(crossinline field: VS.() -> T, crossinline renderer: (T, isFirstRendering: Boolean) -> Unit) {
        val prevViewState = prevViewState
        val currentField = field(currentViewState)
        if (prevViewState == null || currentField !== field(prevViewState)) {
            renderer(currentField, prevViewState == null)
        }
    }
}