package com.qwert2603.andr_lib.base.recyclerview.vh

import android.support.annotation.PluralsRes
import android.view.ViewGroup
import com.qwert2603.andr_lib.R
import com.qwert2603.andr_lib.base.recyclerview.BaseRecyclerViewHolder
import com.qwert2603.andr_lib.base.recyclerview.page_list_item.AllItemsLoaded
import kotlinx.android.synthetic.main.item_all_items_loaded.view.*

class AllItemsLoadedViewHolder(parent: ViewGroup, @PluralsRes private val pluralsRes: Int)
    : BaseRecyclerViewHolder<AllItemsLoaded>(parent, R.layout.item_all_items_loaded) {
    override fun bind(m: AllItemsLoaded) {
        super.bind(m)
        itemView.itemsCount_TextView.text = itemView.resources.getQuantityString(
                pluralsRes,
                m.originalItemsCount,
                m.originalItemsCount
        )
    }
}