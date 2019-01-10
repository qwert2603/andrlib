package com.qwert2603.andrlib.base_mvi

import com.qwert2603.andrlib.base.mvi.BaseView
import com.qwert2603.andrlib.model.ImmediateSchedulersProvider
import com.qwert2603.andrlib.util.LogUtils
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory

class BasePresenterTest {

    @Mock
    private lateinit var view: BaseView<Any>

    @Before
    fun initMocks() {
        LogUtils.logType = LogUtils.LogType.SOUT
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testViewActions() {
        val presenter = JustBasePresenter(ImmediateSchedulersProvider())

        val action_1 = TestAction.Action_1(26)
        presenter.sendAction(action_1)
        Mockito.verifyZeroInteractions(view)

        presenter.attachView(view)

        Mockito.verify(view).render(presenter.initialState)

        Mockito.verify(view).executeAction(action_1)

        val action_2 = TestAction.Action_1(14)
        val action_3 = TestAction.Action_2()

        presenter.sendAction(action_2)
        presenter.sendAction(action_3)

        Mockito.verify(view).executeAction(action_2)
        Mockito.verify(view).executeAction(action_3)

        presenter.detachView()

        Mockito.verifyNoMoreInteractions(view)

        presenter.sendAction(TestAction.Action_1(19))

        Mockito.verifyNoMoreInteractions(view)

        presenter.attachView(view)

        Mockito.verify(view, VerificationModeFactory.times(2)).render(presenter.initialState)

        Mockito.verify(view).executeAction(TestAction.Action_1(19))

        presenter.destroy()

        Mockito.verifyNoMoreInteractions(view)
    }
}

