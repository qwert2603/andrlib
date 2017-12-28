package com.qwert2603.andrlib.base.mvi.load_refresh.list

import com.qwert2603.andrlib.base.mvi.PartialChange
import com.qwert2603.andrlib.model.pagination.Page

/** Partial changes those for view state that extends [ListViewState] */
sealed class ListPartialChange : PartialChange {
    data class NextPageLoading(private val ignored: Unit = Unit) : ListPartialChange()
    data class NextPageError(val t: Throwable) : ListPartialChange()
    data class NextPageCancelled(private val ignored: Unit = Unit) : ListPartialChange()
    data class NextPageLoaded<out T>(val page: Page<T>) : ListPartialChange()
}