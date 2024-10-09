package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

val Float.Companion.INVALID
    inline get() = -1f

val Float.Companion.ZERO
    inline get() = 0f

val Int.Companion.INVALID
    inline get() = -1

val Int.Companion.ZERO
    inline get() = 0

val String.Companion.EMPTY
    inline get() = ""

val String.Companion.NONE
    inline get() = "—"

fun Float?.orInvalid(): Float {
    return this ?: Float.INVALID
}

fun Int?.orInvalid(): Int {
    return this ?: Int.INVALID
}

fun Int?.orZero(): Int {
    return this ?: Int.ZERO
}

fun Int?.toStringOrEmpty(): String {
    return this?.toString().orEmpty()
}

fun Int?.toStringOrNone(): String {
    return this?.toString() ?: String.NONE
}

fun String?.orNone(): String {
    return this ?: String.NONE
}