package com.qwert2603.andrlib.base.mvi

import android.support.annotation.CallSuper
import android.view.View
import com.qwert2603.andrlib.util.LogUtils
import com.hannesdorfmann.mosby3.mvi.MviFragment

abstract class BaseFragment<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>> : MviFragment<V, P>(), BaseView<VS> {

    abstract fun viewForSnackbar(): View?

    private var everRendered = false
    protected var prevViewState: VS? = null
    protected lateinit var currentViewState: VS

    @CallSuper override fun render(vs: VS) {
        if (everRendered) {
            prevViewState = currentViewState
        } else {
            everRendered = true
        }
        currentViewState = vs
        LogUtils.d("${this.javaClass.simpleName} render ${vs.toString().replace('\n', ' ')}")
    }
}