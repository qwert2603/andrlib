package com.qwert2603.andrlib.base.mvi

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.hannesdorfmann.mosby3.FragmentMviDelegateImpl
import com.hannesdorfmann.mosby3.MviDelegateCallback
import com.hannesdorfmann.mosby3.PresenterManager
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.qwert2603.andrlib.util.LogUtils

class FixedFragmentDelegateImpl<V : MvpView, P : MviPresenter<V, *>>(
        delegateCallback: MviDelegateCallback<V, P>,
        private val fragment: Fragment
) : FragmentMviDelegateImpl<V, P>(delegateCallback, fragment) {

    class QVM : ViewModel() {

        lateinit var makeClear: () -> Unit
        lateinit var fragmentName: String

        override fun onCleared() {
            makeClear()
            LogUtils.d("FixedFragmentDelegateImpl QVM makeClear $fragmentName")
        }
    }

    private val qvm: QVM

    init {
        qvm = ViewModelProviders.of(fragment)[QVM::class.java]
        qvm.makeClear = this::makeDestroyPresenter
        qvm.fragmentName = fragment.javaClass.name
    }

    override fun onDestroy() {
        // nth
    }

    private fun makeDestroyPresenter() {
        getty<P>("presenter").destroy()
        val mosbyViewId = getty<String?>("mosbyViewId")
        if (mosbyViewId != null) {
            PresenterManager.remove(fragment.requireActivity(), mosbyViewId)
        }

        setty("presenter", null)
        setty("delegateCallback", null)
        setty("fragment", null)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getty(name: String): T {
        val field = FragmentMviDelegateImpl::class.java.getDeclaredField(name)
        field.isAccessible = true
        return field.get(this) as T
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> setty(name: String, value: T) {
        val field = FragmentMviDelegateImpl::class.java.getDeclaredField(name)
        field.isAccessible = true
        field.set(this, value)
    }
}