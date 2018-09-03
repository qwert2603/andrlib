package com.qwert2603.andrlib.model

interface IdentifiableLong {
    companion object {
        const val NO_ID = -1L
    }

    val id: Long
}