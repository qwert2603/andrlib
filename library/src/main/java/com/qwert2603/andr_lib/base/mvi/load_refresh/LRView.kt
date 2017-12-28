package com.qwert2603.andr_lib.base.mvi.load_refresh

import com.qwert2603.andr_lib.base.mvi.BaseView
import io.reactivex.Observable

interface LRView<in VS : LRViewState> : BaseView<VS> {
    fun load(): Observable<Any>
    fun retry(): Observable<Any>
    fun refresh(): Observable<Any>
}