package com.qwert2603.andrlib.base.recyclerview

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.qwert2603.andrlib.model.IdentifiableLong
import com.qwert2603.andrlib.util.inflate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseRecyclerViewHolder<M : IdentifiableLong>(parent: ViewGroup, @LayoutRes layoutRes: Int)
    : RecyclerView.ViewHolder(parent.inflate(layoutRes)) {

    var adapter: BaseRecyclerViewAdapter<M>? = null

    var m: M? = null

    private val viewDisposable = CompositeDisposable()

    init {
        itemView.setOnClickListener { _ ->
            adapter?.let {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                if (adapterPosition < it.adapterList.modelList.size) {
                    it.modelItemClicks.onNext(it.adapterList.getModelItem(adapterPosition))
                } else {
                    it.pageIndicatorClicks.onNext(it.adapterList.pageIndicator!!)
                }
            }
        }
        itemView.setOnLongClickListener(View.OnLongClickListener { _ ->
            adapter?.let {
                if (adapterPosition == RecyclerView.NO_POSITION) return@OnLongClickListener false
                if (adapterPosition < it.adapterList.modelList.size) {
                    it.modelItemLongClicks.onNext(it.adapterList.getModelItem(adapterPosition))
                    return@OnLongClickListener it.modelItemLongClicks.hasObservers()
                } else {
                    it.pageIndicatorLongClicks.onNext(it.adapterList.pageIndicator!!)
                    return@OnLongClickListener it.pageIndicatorLongClicks.hasObservers()
                }
            }
            return@OnLongClickListener false
        })
    }

    @CallSuper
    open fun bind(m: M) {
        this.m = m
    }

    @CallSuper
    open fun onRecycled() {
        viewDisposable.clear()
        m = null
    }

    protected fun Disposable.disposeOnRecycled() {
        viewDisposable.add(this)
    }
}
