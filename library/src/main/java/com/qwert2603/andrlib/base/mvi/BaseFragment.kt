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

    @CallSuper override fun render(vs: VS) {
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
}