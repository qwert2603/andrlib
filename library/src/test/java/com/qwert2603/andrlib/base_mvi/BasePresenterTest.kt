package com.qwert2603.andrlib.base_mvi

import com.qwert2603.andrlib.base.mvi.BaseView
import com.qwert2603.andrlib.model.ImmediateSchedulersProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class BasePresenterTest {

    @Mock private lateinit var view: BaseView<Any>

    @Before
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testViewActions() {
        val presenter = JustBasePresenter(ImmediateSchedulersProvider())
        presenter.attachView(view)

        Mockito.verifyZeroInteractions(view)

        val action_2 = TestAction.Action_2()

        presenter.sendAction(TestAction.Action_1(14))
        presenter.sendAction(action_2)

        Mockito.verify(view).executeAction(TestAction.Action_1(14))
        Mockito.verify(view).executeAction(action_2)

        presenter.detachView()

        presenter.sendAction(TestAction.Action_1(19))

        Mockito.verifyNoMoreInteractions(view)

        presenter.attachView(view)

        Mockito.verify(view).executeAction(TestAction.Action_1(19))

        presenter.destroy()

        Mockito.verifyNoMoreInteractions(view)
    }
}

