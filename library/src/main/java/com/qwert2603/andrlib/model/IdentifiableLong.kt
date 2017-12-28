package com.qwert2603.andrlib.model

interface IdentifiableLong {
    companion object {
        val NO_ID = -1L
    }

    val id: Long
}