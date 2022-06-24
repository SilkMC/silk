package net.silkmc.silk.core.kotlin

val <T : Comparable<T>> Pair<T, T>.min
    get() = if (first <= second) first else second

val <T : Comparable<T>> Pair<T, T>.max
    get() = if (first >= second) first else second
