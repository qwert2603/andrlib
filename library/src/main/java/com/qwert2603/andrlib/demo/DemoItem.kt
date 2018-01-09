package com.qwert2603.andrlib.demo

import com.qwert2603.andrlib.model.IdentifiableLong

data class DemoItem(
        override val id: Long,
        val i: Int
) : IdentifiableLong