package com.qwert2603.andrlib.base_mvi

import com.qwert2603.andrlib.base.mvi.ViewAction

sealed class TestAction : ViewAction {
    data class Action_1(val i: Int) : TestAction()
    class Action_2 : TestAction()
}