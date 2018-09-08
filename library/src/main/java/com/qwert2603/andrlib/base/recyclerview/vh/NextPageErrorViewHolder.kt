package com.qwert2603.andrlib.base.recyclerview.vh

import android.view.ViewGroup
import com.qwert2603.andrlib.R
import com.qwert2603.andrlib.base.recyclerview.BaseRecyclerViewHolder
import com.qwert2603.andrlib.base.recyclerview.page_list_item.NextPageError
import kotlinx.android.synthetic.main.item_next_page_error.view.*

class NextPageErrorViewHolder(parent: ViewGroup)
    : BaseRecyclerViewHolder<NextPageError>(parent, R.layout.item_next_page_error), PageIndicatorViewHolder {
    init {
        itemView.retry_Button.setOnClickListener {
            adapter?.pageIndicatorErrorRetryClicks?.onNext(Any())
        }
    }
}