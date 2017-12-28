package com.qwert2603.andrlib.schedulers

import io.reactivex.Scheduler

interface ModelSchedulersProvider {
    val io: Scheduler
    val computation: Scheduler
}