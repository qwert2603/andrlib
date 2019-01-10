package com.qwert2603.andrlib.base_mvi

import com.qwert2603.andrlib.base.mvi.BasePresenter
import com.qwert2603.andrlib.base.mvi.BaseView
import com.qwert2603.andrlib.base.mvi.PartialChange
import com.qwert2603.andrlib.base.mvi.ViewAction
import com.qwert2603.andrlib.schedulers.UiSchedulerProvider
import io.reactivex.Observable

class JustBasePresenter(uiSchedulerProvider: UiSchedulerProvider) : BasePresenter<BaseView<Any>, Any>(uiSchedulerProvider) {

    fun sendAction(action: ViewAction) {
        viewActions.onNext(action)
    }

    public override val initialState = Any()

    override val partialChanges: Observable<PartialChange> = Observable.never()

    override fun stateReducer(vs: Any, change: PartialChange) = vs
}