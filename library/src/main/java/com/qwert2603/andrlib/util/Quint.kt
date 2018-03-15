package com.qwert2603.andrlib.util

data class Quint<out A, out B, out C, out D, out E>(
        val first: A,
        val second: B,
        val third: C,
        val forth: D,
        val fifth: E
)

fun <A, B, C, D, E> Triple<A, B, C>.toQuint(forth: D, fifth: E) = Quint(first, second, third, forth, fifth)
fun <A, B, C, D, E> Quad<A, B, C, D>.toQuint(fifth: E) = Quint(first, second, third, forth, fifth)