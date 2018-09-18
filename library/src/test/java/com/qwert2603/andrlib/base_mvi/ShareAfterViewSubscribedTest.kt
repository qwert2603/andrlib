package com.qwert2603.andrlib.base_mvi

import com.qwert2603.andrlib.base.mvi.BasePresenter
import com.qwert2603.andrlib.base.mvi.BaseView
import com.qwert2603.andrlib.base.mvi.PartialChange
import com.qwert2603.andrlib.base.mvi.ViewAction
import com.qwert2603.andrlib.model.ImmediateSchedulersProvider
import com.qwert2603.andrlib.util.LogUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ShareAfterViewSubscribedTest {

    @Before
    fun initMocks() {
        LogUtils.logType = LogUtils.LogType.SOUT
    }

    data class SomeVS(val i: Int)

    interface SomeView : BaseView<SomeVS> {
        fun someIntent(): Observable<Int>
    }

    sealed class SomePC : PartialChange {
        data class ChangeI(val a: Int) : SomePC()
    }

    class SomePresenter : BasePresenter<SomeView, SomeVS>(ImmediateSchedulersProvider()) {

        private val someIntent = intent { it.someIntent() }
                .startWith(1)
//                .share()
                .shareAfterViewSubscribed()

        override val partialChanges: Observable<PartialChange> = Observable.merge(
                someIntent.map { SomePC.ChangeI(it * 2) },
                someIntent.map { SomePC.ChangeI(it * 2 + 1) }
        )

        override val initialState = SomeVS(0)

        override fun stateReducer(vs: SomeVS, change: PartialChange): SomeVS {
            pcs += change
            return vs
        }

        val pcs = mutableListOf<PartialChange>()
    }

    class SomeViewImpl(private val someIntent: Observable<Int>) : SomeView {
        override fun someIntent(): Observable<Int> = someIntent
        override fun render(vs: SomeVS) = Unit
        override fun executeAction(va: ViewAction) = Unit
    }

    @Test
    fun testShareAfterViewSubscribed() {
        val someIntent = PublishSubject.create<Int>()
        val view = SomeViewImpl(someIntent)
        val presenter = SomePresenter()

        presenter.attachView(view)

        someIntent.onNext(5)

        Assert.assertEquals(listOf(2, 3, 10, 11).map { SomePC.ChangeI(it) }, presenter.pcs)
    }

}
