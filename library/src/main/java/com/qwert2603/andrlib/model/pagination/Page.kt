package com.qwert2603.andrlib.model.pagination

data class Page<out T>(
        val list: List<T>,
        val allItemsLoaded: Boolean
)