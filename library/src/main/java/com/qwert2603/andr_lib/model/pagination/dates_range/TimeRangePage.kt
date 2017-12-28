package com.qwert2603.andr_lib.model.pagination.dates_range

data class TimeRangePage<out T>(
        val timeRangeKey: TimeRangeKey,
        val list: List<T>,
        val allItemsLoaded: Boolean
)