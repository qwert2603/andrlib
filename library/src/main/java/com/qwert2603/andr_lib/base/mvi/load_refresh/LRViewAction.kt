package com.qwert2603.andr_lib.base.mvi.load_refresh

import com.qwert2603.andr_lib.base.mvi.ViewAction

/** Action that can be applied to [LRView]. */
sealed class LRViewAction : ViewAction {
    data class RefreshingError(val t: Throwable) : LRViewAction()
}