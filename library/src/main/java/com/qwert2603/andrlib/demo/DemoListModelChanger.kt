package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.base.mvi.load_refresh.list.ListModel
import com.qwert2603.andrlib.base.mvi.load_refresh.list.ListModelChanger
import com.qwert2603.andrlib.base.mvi.load_refresh.list.ListViewState

class DemoListModelChanger : ListModelChanger {
    @Suppress("UNCHECKED_CAST")
    override fun <VS : ListViewState<*>> changeListModel(vs: VS, listModel: ListModel): VS = when (vs) {
        is DemoViewState -> vs.copy(listModel = listModel)
        else -> null!!
    } as VS
}