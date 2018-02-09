package com.qwert2603.andrlib.util

data class Quadruple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val forth: D
)

fun <A, B, C, D> Triple<A, B, C>.toQuadruple(d: D) = Quadruple(first, second, third, d)