package com.qwert2603.andrlib.model

fun String.hashCodeLong() =
        if (isEmpty()) -659209360301145L
        else filterIndexed { index, _ -> index % 2 == 0 }.hashCode().toLong() +
                (filterIndexed { index, _ -> index % 2 == 1 }.hashCode().toLong() shl Integer.SIZE)