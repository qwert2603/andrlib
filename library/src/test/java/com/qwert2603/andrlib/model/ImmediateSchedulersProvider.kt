package com.qwert2603.andrlib.model

import com.qwert2603.andrlib.schedulers.ModelSchedulersProvider
import com.qwert2603.andrlib.schedulers.UiSchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ImmediateSchedulersProvider : UiSchedulerProvider, ModelSchedulersProvider {
    override val ui: Scheduler = Schedulers.trampoline()
    override fun isOnUi() = true
    override val io: Scheduler = Schedulers.trampoline()
    override val computation: Scheduler = Schedulers.trampoline()
}