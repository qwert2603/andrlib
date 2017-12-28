package com.qwert2603.andrlib.base.mvi.load_refresh

import com.qwert2603.andrlib.base.mvi.BaseView
import io.reactivex.Observable

interface LRView<in VS : LRViewState> : BaseView<VS> {
    fun load(): Observable<Any>
    fun retry(): Observable<Any>
    fun refresh(): Observable<Any>
}