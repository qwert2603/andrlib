package com.qwert2603.andrlib.base.mvi.load_refresh

import io.reactivex.Observable

interface LoadRefreshPanel {
    fun retryClicks(): Observable<Any>
    fun refreshes(): Observable<Any>

    fun render(vs: LRViewState)
}