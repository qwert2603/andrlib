package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.base.mvi.PartialChange
import com.qwert2603.andrlib.base.mvi.load_refresh.LRPresenter
import com.qwert2603.andrlib.schedulers.UiSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

class DemoPresenter(
        uiSchedulerProvider: UiSchedulerProvider
) : LRPresenter<Any, String, DemoViewState, DemoView>(uiSchedulerProvider) {

    override val initialState = DemoViewState(EMPTY_LR_MODEL, 42, "fish")

    override fun DemoViewState.applyInitialModel(i: String) = copy(s = i)

    override fun initialModelSingle(additionalKey: Any): Single<String> = Single.just("home")

    override val partialChanges: Observable<PartialChange> = Observable.never()
}