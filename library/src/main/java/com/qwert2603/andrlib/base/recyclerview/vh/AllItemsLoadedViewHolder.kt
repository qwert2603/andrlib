package com.qwert2603.andrlib.base.recyclerview.vh

import android.view.ViewGroup
import com.qwert2603.andrlib.R
import com.qwert2603.andrlib.base.recyclerview.BaseRecyclerViewHolder
import com.qwert2603.andrlib.base.recyclerview.page_list_item.AllItemsLoaded
import kotlinx.android.synthetic.main.item_all_items_loaded.view.*

class AllItemsLoadedViewHolder(parent: ViewGroup)
    : BaseRecyclerViewHolder<AllItemsLoaded>(parent, R.layout.item_all_items_loaded) {
    override fun bind(m: AllItemsLoaded) {
        super.bind(m)
        itemView.itemsCount_TextView.text = itemView.resources.getQuantityString(
                adapter?.pluralsRes() ?: R.plurals.items,
                m.originalItemsCount,
                m.originalItemsCount
        )
    }
}