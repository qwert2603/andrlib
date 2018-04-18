package com.qwert2603.andrlib.base.mvi

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.CallSuper
import android.util.AttributeSet
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.hannesdorfmann.mosby3.mvi.layout.MviFrameLayout
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.Quad
import com.qwert2603.andrlib.util.Quint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFrameLayout<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>> : MviFrameLayout<V, P>, BaseView<VS>, FragmentForSnackbar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    abstract override fun viewForSnackbar(): View?

    private var everRendered = false

    protected var prevViewState: VS? = null

    protected lateinit var currentViewState: VS

    private val viewDisposable = CompositeDisposable()

    override fun onAttachedToWindow() {
        everRendered = false
        prevViewState = null
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        viewDisposable.clear()
        super.onDetachedFromWindow()
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

    protected inline fun <T, U, V, W> renderIfChangedFour(crossinline fields: VS.() -> Quad<T, U, V, W>, crossinline renderer: (Quad<T, U, V, W>) -> Unit) {
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

    protected inline fun <T, U, V, W, X> renderIfChangedFive(crossinline fields: VS.() -> Quint<T, U, V, W, X>, crossinline renderer: (Quint<T, U, V, W, X>) -> Unit) {
        val prevViewState = prevViewState
        val currentField = fields(currentViewState)
        if (prevViewState == null) {
            renderer(currentField)
            return
        }
        val prevField = fields(prevViewState)
        if (currentField.first !== prevField.first || currentField.second !== prevField.second
                || currentField.third !== prevField.third || currentField.forth !== prevField.forth
                || currentField.fifth !== prevField.fifth) {
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