package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.base.mvi.load_refresh.LRModel
import com.qwert2603.andrlib.base.mvi.load_refresh.LRModelChanger
import com.qwert2603.andrlib.base.mvi.load_refresh.LRViewState

class DemoLRModelChanger : LRModelChanger {
    @Suppress("UNCHECKED_CAST")
    override fun <VS : LRViewState> changeLRModel(vs: VS, lrModel: LRModel): VS = when (vs) {
        is DemoViewState -> vs.copy(lrModel = lrModel)
        else -> null!!
    } as VS
}