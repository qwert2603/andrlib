package com.qwert2603.andrlib.base.recyclerview

import android.support.annotation.PluralsRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.qwert2603.andrlib.R
import com.qwert2603.andrlib.base.recyclerview.page_list_item.AllItemsLoaded
import com.qwert2603.andrlib.base.recyclerview.page_list_item.NextPageError
import com.qwert2603.andrlib.base.recyclerview.page_list_item.NextPageLoading
import com.qwert2603.andrlib.base.recyclerview.page_list_item.PageListItem
import com.qwert2603.andrlib.base.recyclerview.vh.AllItemsLoadedViewHolder
import com.qwert2603.andrlib.base.recyclerview.vh.NextPageErrorViewHolder
import com.qwert2603.andrlib.base.recyclerview.vh.NextPageLoadingViewHolder
import com.qwert2603.andrlib.model.IdentifiableLong
import com.qwert2603.andrlib.util.LogUtils
import io.reactivex.subjects.PublishSubject

/**
 * Base adapter that contains list of model items and can show page indicator items.
 *
 * Page indicator items are:
 * [NextPageLoadingViewHolder]
 * [NextPageErrorViewHolder]
 * [AllItemsLoadedViewHolder]
 *
 * Classes-inheritor should override [getItemViewTypeModel], [onCreateViewHolderModel] and [onBindViewHolderModel]
 * for view-type defining, creating and binding model-ViewHolders.
 *
 * Also classes inheritors can specify custom id for some model item.
 * See [getItemIdModel] for details.
 */
abstract class BaseRecyclerViewAdapter<M : IdentifiableLong> : RecyclerView.Adapter<BaseRecyclerViewHolder<IdentifiableLong>>() {

    companion object {
        const val VIEW_TYPE_NEXT_PAGE_LOADING = -1
        const val VIEW_TYPE_NEXT_PAGE_ERROR = -2
        const val VIEW_TYPE_ALL_ITEMS_LOADED = -3
    }

    var useDiffUtils = true

    var recyclerView: RecyclerView? = null

    data class AdapterList<out M : IdentifiableLong>(
            val modelList: List<M>,
            val pageIndicator: PageListItem? = null
    ) {
        val size = modelList.size + if (pageIndicator != null) 1 else 0

        operator fun get(position: Int): IdentifiableLong {
            if (position == modelList.size && pageIndicator != null) return pageIndicator
            return modelList[position]
        }

        fun getModelItem(position: Int): M = modelList[position]
    }

    var adapterList: AdapterList<M> = AdapterList(emptyList(), null)
        set(value) {
            val old = field
            field = value

            if (useDiffUtils) {
                DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize() = old.size
                    override fun getNewListSize() = field.size
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = getItemId(old[oldItemPosition]) == getItemId(field[newItemPosition])
                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == field[newItemPosition]
                }).dispatchUpdatesTo(this)
            } else {
                notifyDataSetChanged()
            }
        }

    var modelItemClicks: PublishSubject<M> = PublishSubject.create()
    var modelItemLongClicks: PublishSubject<M> = PublishSubject.create()

    var pageIndicatorClicks: PublishSubject<PageListItem> = PublishSubject.create()
    var pageIndicatorLongClicks: PublishSubject<PageListItem> = PublishSubject.create()
    var pageIndicatorErrorRetryClicks: PublishSubject<Any> = PublishSubject.create()

    @PluralsRes
    open fun pluralsRes() = R.plurals.items

    init {
        setHasStableIds(true)
    }

    final override fun setHasStableIds(hasStableIds: Boolean) {
        if (hasStableIds == this.hasStableIds()) return
        super.setHasStableIds(hasStableIds)
    }

    final override fun getItemViewType(position: Int) = when (adapterList[position]) {
        is NextPageLoading -> VIEW_TYPE_NEXT_PAGE_LOADING
        is NextPageError -> VIEW_TYPE_NEXT_PAGE_ERROR
        is AllItemsLoaded -> VIEW_TYPE_ALL_ITEMS_LOADED
        else -> getItemViewTypeModel(adapterList.getModelItem(position))
    }

    @Suppress("UNCHECKED_CAST")
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<IdentifiableLong> = when (viewType) {
        VIEW_TYPE_NEXT_PAGE_LOADING -> NextPageLoadingViewHolder(parent)
        VIEW_TYPE_NEXT_PAGE_ERROR -> NextPageErrorViewHolder(parent)
        VIEW_TYPE_ALL_ITEMS_LOADED -> AllItemsLoadedViewHolder(parent)
        else -> onCreateViewHolderModel(parent, viewType)
    } as BaseRecyclerViewHolder<IdentifiableLong>

    @Suppress("UNCHECKED_CAST")
    final override fun onBindViewHolder(holder: BaseRecyclerViewHolder<IdentifiableLong>, position: Int) {
        holder.adapter = this as BaseRecyclerViewAdapter<IdentifiableLong>
        if (position < adapterList.modelList.size) {
            onBindViewHolderModel(holder, position)
        } else {
            holder.bind(adapterList.pageIndicator!!)
        }
    }

    /**
     * @return view type for model [m].
     * must be >= 0.
     */
    open fun getItemViewTypeModel(m: M) = 0

    @Suppress("UNCHECKED_CAST")
    private fun getItemId(item: IdentifiableLong): Long {
        if (item is PageListItem) return item.id
        return getItemIdModel(item as M) ?: item.id
    }

    /**
     * todo: this is bad!
     * @return custom id for model item.
     * if null, [IdentifiableLong.id] will be used as item's id.
     * by default returns null.
     */
    open fun getItemIdModel(m: M): Long? = null

    abstract fun onCreateViewHolderModel(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<M>

    open fun onBindViewHolderModel(holder: BaseRecyclerViewHolder<M>, position: Int) {
        holder.bind(adapterList.getModelItem(position))
    }

    override fun onViewRecycled(holder: BaseRecyclerViewHolder<IdentifiableLong>) {
        holder.adapter = null
        holder.onRecycled()
        super.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: BaseRecyclerViewHolder<IdentifiableLong>): Boolean {
        LogUtils.e("BaseRecyclerViewAdapter onFailedToRecycleView $holder")
        return super.onFailedToRecycleView(holder)
    }

    final override fun getItemCount() = adapterList.size

    final override fun getItemId(position: Int): Long = getItemId(adapterList[position])
}