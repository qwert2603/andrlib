package com.qwert2603.andr_lib.base.mvi.load_refresh.list

data class ListModel(
        val nextPageLoading: Boolean,
        val nextPageError: Throwable?,
        val allItemsLoaded: Boolean
)