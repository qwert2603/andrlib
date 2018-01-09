package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.base.mvi.PartialChange
import com.qwert2603.andrlib.base.mvi.load_refresh.list.ListPresenter
import com.qwert2603.andrlib.model.pagination.Page
import com.qwert2603.andrlib.schedulers.UiSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

class DemoPresenter(
        uiSchedulerProvider: UiSchedulerProvider
) : ListPresenter<Any, String, DemoViewState, DemoView, DemoItem>(uiSchedulerProvider) {

    override val initialState = DemoViewState(EMPTY_LR_MODEL, EMPTY_LIST_MODEL, emptyList(), 42, "fish")

    override fun DemoViewState.applyInitialModel(i: String) = copy(s = i)

    override fun DemoViewState.addNextPage(nextPage: List<DemoItem>) = copy(showingList = showingList + nextPage)

    override fun initialModelSingle(additionalKey: Any): Single<String> = Single.just("home")

    override fun nextPageSingle(): Single<Page<DemoItem>> = Single.just(Page(emptyList(), true))

    override val partialChanges: Observable<PartialChange> = Observable.never()
}