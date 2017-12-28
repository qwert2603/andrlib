package com.qwert2603.andr_lib.model.pagination

data class Page<out T>(
        val list: List<T>,
        val allItemsLoaded: Boolean
)