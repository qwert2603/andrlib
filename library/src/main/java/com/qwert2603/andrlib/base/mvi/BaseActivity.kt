package com.qwert2603.andrlib.base.mvi

import android.support.annotation.CallSuper
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.StateHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>> :
        MviActivity<V, P>(), BaseView<VS>, StateHolder<VS> {

    private var everRendered = false

    override var prevViewState: VS? = null

    override lateinit var currentViewState: VS

    private val startStopDisposable = CompositeDisposable()

    override fun onStart() {
        everRendered = false
        prevViewState = null
        super.onStart()
    }

    override fun onStop() {
        startStopDisposable.clear()
        super.onStop()
    }

    @CallSuper
    override fun render(vs: VS) {
        if (everRendered) {
            prevViewState = currentViewState
        } else {
            everRendered = true
        }
        currentViewState = vs
        LogUtils.d { "${this.javaClass.simpleName} render ${vs.toString().replace('\n', ' ')}" }
    }

    protected fun Disposable.disposeOnStop() {
        startStopDisposable.add(this)
    }

    protected fun renderAll() {
        everRendered = false
        prevViewState = null
        render(currentViewState)
    }
}