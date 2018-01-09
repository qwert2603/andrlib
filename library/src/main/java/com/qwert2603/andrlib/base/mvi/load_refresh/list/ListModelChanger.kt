package com.qwert2603.andrlib.base.mvi.load_refresh.list

interface ListModelChanger {
    fun <VS : ListViewState<*>> changeListModel(vs: VS, listModel: ListModel): VS
}

lateinit var listModelChangerInstance: ListModelChanger

fun <VS : ListViewState<*>> VS.changeListModel(listModel: ListModel) = listModelChangerInstance.changeListModel(this, listModel)