package com.qwert2603.andrlib.base.mvi

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.hannesdorfmann.mosby3.FragmentMviDelegate
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.qwert2603.andrlib.util.LogUtils
import com.qwert2603.andrlib.util.StateHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<VS : Any, V : BaseView<VS>, P : BasePresenter<V, VS>> :
        MviFragment<V, P>(), BaseView<VS>, StateHolder<VS> {

    override fun getMvpDelegate(): FragmentMviDelegate<V, P> {
        if (mvpDelegate == null) {
            mvpDelegate = FixedFragmentDelegateImpl(this, this)
        }

        return mvpDelegate
    }

    private var everRendered = false

    override var prevViewState: VS? = null

    override lateinit var currentViewState: VS

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
        LogUtils.d { "${this.javaClass.simpleName} render ${vs.toString().replace('\n', ' ')}" }
    }

    protected fun Disposable.disposeOnDestroyView() {
        viewDisposable.add(this)
    }

    protected fun renderAll() {
        everRendered = false
        prevViewState = null
        render(currentViewState)
    }
}