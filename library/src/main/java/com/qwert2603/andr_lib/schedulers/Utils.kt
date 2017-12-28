package com.qwert2603.andr_lib.schedulers

import io.reactivex.Observable

fun <T> Observable<T>.switchToUiIfNotYet(uiSchedulerProvider: UiSchedulerProvider): Observable<T> = this
        .concatMap {
            Observable.just(it)
                    .compose { if (uiSchedulerProvider.isOnUi()) it else it.observeOn(uiSchedulerProvider.ui) }
        }