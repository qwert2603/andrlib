package com.qwert2603.andr_lib.schedulers

import io.reactivex.Scheduler

interface ModelSchedulersProvider {
    val io: Scheduler
    val computation: Scheduler
}