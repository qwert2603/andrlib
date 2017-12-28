package com.qwert2603.andr_lib.base.mvi.load_refresh.list

import com.qwert2603.andr_lib.base.mvi.load_refresh.LRView
import io.reactivex.Observable

interface ListView<in VS : ListViewState<*>> : LRView<VS> {
    fun loadNextPage(): Observable<Any>
}