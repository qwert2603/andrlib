package com.qwert2603.andr_lib.base.mvi

import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseView<in VS> : MvpView {
    fun render(vs: VS)
    fun executeAction(va: ViewAction)
}