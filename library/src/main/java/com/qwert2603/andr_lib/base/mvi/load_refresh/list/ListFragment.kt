package com.qwert2603.andr_lib.base.mvi.load_refresh.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qwert2603.andr_lib.base.mvi.BasePresenter
import com.qwert2603.andr_lib.base.mvi.load_refresh.LRFragment
import com.qwert2603.andr_lib.base.recyclerview.BaseRecyclerViewAdapter
import com.qwert2603.andr_lib.model.IdentifiableLong
import com.qwert2603.andr_lib.util.showIfNotYet
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.include_list.*

abstract class ListFragment<VS : ListViewState<T>, V : ListView<VS>, P : BasePresenter<V, VS>, T : IdentifiableLong>
    : LRFragment<VS, V, P>(), ListView<VS> {

    companion object {
        private const val LAYER_NOTHING = 0
        private const val LAYER_EMPTY = 1
        private const val LAYER_LIST = 2
    }

    abstract protected val adapter: BaseRecyclerViewAdapter<T>

    open protected fun createLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_RecyclerView.layoutManager = createLayoutManager()
    }

    override fun onDestroyView() {
        adapter.recyclerView = null
        super.onDestroyView()
    }

    override fun loadNextPage(): Observable<Any> = RxView.preDraws(list_RecyclerView, { true })
            .filter {
                with(currentViewState.lrModel) { isModelLoaded && !refreshing } &&
                        with(currentViewState.listModel) { !allItemsLoaded && !nextPageLoading && nextPageError == null }
            }
            .filter {
                val linearLayoutManager = list_RecyclerView.layoutManager as? LinearLayoutManager ?: return@filter false
                linearLayoutManager.findLastVisibleItemPosition() > adapter.itemCount - 5
            }
            .mergeWith(adapter.pageIndicatorErrorRetryClicks)

    override fun render(vs: VS) {
        super.render(vs)

        // we need to set adapter.modelList = emptyList() and list_RecyclerView.adapter = null,
        // to prevent from showing old list when new list is showing after loading or showing "list is empty".
        if (vs.lrModel.isModelLoaded) {
            if (vs.showingList.isEmpty() && vs.listModel.allItemsLoaded) {
                if (list_RecyclerView.adapter != null) {
                    adapter.adapterList = BaseRecyclerViewAdapter.AdapterList(emptyList())
                    adapter.notifyDataSetChanged()
                    list_RecyclerView.adapter = null
                    adapter.recyclerView = null
                }
                list_ViewAnimator.showIfNotYet(LAYER_EMPTY)
            } else {
                if (list_RecyclerView.adapter == null) {
                    list_RecyclerView.adapter = adapter
                    adapter.recyclerView = list_RecyclerView
                }
                adapter.adapterList = BaseRecyclerViewAdapter.AdapterList(vs.showingList, vs.pageIndicatorItem())
                list_ViewAnimator.showIfNotYet(LAYER_LIST)
            }
        } else {
            if (list_RecyclerView.adapter != null) {
                adapter.adapterList = BaseRecyclerViewAdapter.AdapterList(emptyList())
                adapter.notifyDataSetChanged()
                list_RecyclerView.adapter = null
                adapter.recyclerView = null
            }
            list_ViewAnimator.showIfNotYet(LAYER_NOTHING, animate = false)
        }
    }

    protected val _list_RecyclerView: RecyclerView get() = list_RecyclerView
}