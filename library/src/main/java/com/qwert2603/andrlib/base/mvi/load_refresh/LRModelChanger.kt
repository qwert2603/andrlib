package com.qwert2603.andrlib.base.mvi.load_refresh

interface LRModelChanger {
    fun <VS : LRViewState> changeLRModel(vs: VS, lrModel: LRModel): VS
}

lateinit var lrModelChangerInstance: LRModelChanger

fun <VS : LRViewState> VS.changeLRModel(lrModel: LRModel) = lrModelChangerInstance.changeLRModel(this, lrModel)