package com.qwert2603.andrlib.base.mvi.load_refresh

import com.qwert2603.andrlib.demo.DemoLRModelChanger

interface LRModelChanger {
    fun <VS : LRViewState> changeLRModel(vs: VS, lrModel: LRModel): VS
}

val lrModelChangerInstance: LRModelChanger = DemoLRModelChanger()

fun <VS : LRViewState> VS.changeLRModel(lrModel: LRModel) = lrModelChangerInstance.changeLRModel(this, lrModel)