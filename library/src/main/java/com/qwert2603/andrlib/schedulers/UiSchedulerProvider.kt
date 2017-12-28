package com.qwert2603.andrlib.schedulers

import io.reactivex.Scheduler

interface UiSchedulerProvider {
    val ui: Scheduler
    fun isOnUi(): Boolean
}