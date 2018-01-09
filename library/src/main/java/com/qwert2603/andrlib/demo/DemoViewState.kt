package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.base.mvi.load_refresh.LRModel
import com.qwert2603.andrlib.base.mvi.load_refresh.LRViewState
import com.qwert2603.andrlib.base.mvi.load_refresh.list.ListModel
import com.qwert2603.andrlib.base.mvi.load_refresh.list.ListViewState
import com.qwert2603.andrlib.generator.GenerateLRChanger
import com.qwert2603.andrlib.generator.GenerateListChanger

@GenerateLRChanger
@GenerateListChanger
data class DemoViewState(
        override val lrModel: LRModel,
        override val listModel: ListModel,
        override val showingList: List<DemoItem>,
        val i: Int,
        val s: String
) : LRViewState, ListViewState<DemoItem>