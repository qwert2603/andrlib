package com.qwert2603.andrlib.base.mvi

import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.support.annotation.CallSuper
import android.util.AttributeSet
import com.hannesdorfmann.mosby3.mvi.layout.MviFrameLayout
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.StateHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFrameLayout<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>>
    : MviFrameLayout<V, P>, BaseView<VS>, StateHolder<VS> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var everRendered = false

    override var prevViewState: VS? = null

    override lateinit var currentViewState: VS

    private val viewDisposable = CompositeDisposable()

    override fun onAttachedToWindow() {
        everRendered = false
        prevViewState = null
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        viewDisposable.clear()
        (mviDelegate as? Application.ActivityLifecycleCallbacks)
                ?.let { (context as Activity).application.unregisterActivityLifecycleCallbacks(it) }
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
        LogUtils.d { "${this.javaClass.simpleName} render ${vs.toString().replace('\n', ' ')}" }
    }

    protected fun Disposable.disposeOnDetachView() {
        viewDisposable.add(this)
    }

    protected fun renderAll() {
        everRendered = false
        prevViewState = null
        render(currentViewState)
    }
}