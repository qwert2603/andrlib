package com.qwert2603.andrlib.util

data class Quad<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val forth: D
)

fun <A, B, C, D> Triple<A, B, C>.toQuad(forth: D) = Quad(first, second, third, forth)