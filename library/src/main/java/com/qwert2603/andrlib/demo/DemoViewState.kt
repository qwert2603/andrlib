package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.base.mvi.load_refresh.LRModel
import com.qwert2603.andrlib.base.mvi.load_refresh.LRViewState

data class DemoViewState(
        override val lrModel: LRModel,
        val i: Int,
        val s: String
) : LRViewState