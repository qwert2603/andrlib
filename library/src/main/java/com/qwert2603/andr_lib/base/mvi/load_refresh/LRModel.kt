package com.qwert2603.andr_lib.base.mvi.load_refresh

data class LRModel(
        val loading: Boolean,
        val loadingError: Throwable?,
        val canRefresh: Boolean,
        val refreshing: Boolean,
        val refreshingError: Throwable?
) {
    val isModelLoaded = !loading && loadingError == null
}