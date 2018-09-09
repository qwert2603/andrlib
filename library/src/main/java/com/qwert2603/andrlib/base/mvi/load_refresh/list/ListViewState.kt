package com.qwert2603.andrlib.base.mvi.load_refresh.list

import com.qwert2603.andrlib.base.mvi.load_refresh.LRViewState
import com.qwert2603.andrlib.base.recyclerview.page_list_item.AllItemsLoaded
import com.qwert2603.andrlib.base.recyclerview.page_list_item.NextPageError
import com.qwert2603.andrlib.base.recyclerview.page_list_item.NextPageLoading
import com.qwert2603.andrlib.base.recyclerview.page_list_item.PageListItem
import com.qwert2603.andrlib.base.recyclerview.vh.AllItemsLoadedViewHolder
import com.qwert2603.andrlib.model.IdentifiableLong

interface ListViewState<out T : IdentifiableLong> : LRViewState {
    val listModel: ListModel
    val showingList: List<T>


    fun pageIndicatorItem(): PageListItem? = when {
        listModel.nextPageLoading -> NextPageLoading()
        listModel.nextPageError != null -> NextPageError()
        listModel.allItemsLoaded -> AllItemsLoaded(originalItemsCount())
        else -> null
    }

    /**
     * @return count of items that loaded by pages.
     * For example, if feed list consist of news items and date items
     * (news items are loaded by pages and date items are added locally after grouping news items),
     * then [originalItemsCount] == count of news items.
     * This "count" also will be shown in [AllItemsLoadedViewHolder]. See [AllItemsLoaded.originalItemsCount].
     */
    fun originalItemsCount() = showingList.size
}