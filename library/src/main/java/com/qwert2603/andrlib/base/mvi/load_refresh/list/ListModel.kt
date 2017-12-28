package com.qwert2603.andrlib.base.mvi.load_refresh.list

data class ListModel(
        val nextPageLoading: Boolean,
        val nextPageError: Throwable?,
        val allItemsLoaded: Boolean
)