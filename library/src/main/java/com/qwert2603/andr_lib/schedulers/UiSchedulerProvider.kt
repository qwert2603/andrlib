package com.qwert2603.andr_lib.schedulers

import io.reactivex.Scheduler

interface UiSchedulerProvider {
    val ui: Scheduler
    fun isOnUi(): Boolean
}