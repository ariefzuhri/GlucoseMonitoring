package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import kotlin.math.roundToInt

fun <T> Collection<T>.averageOfOrNull(selector: (T) -> Number): Float? {
    return if (this.isNotEmpty()) {
        val sum = this.sumOf { selector(it).toDouble() }
        (sum / this.size).toFloat()
    } else null
}

fun Int?.minus(other: Int?): Int? {
    return if (this != null && other != null) this - other else null
}

fun cmToMeters(cm: Int): Float {
    return cm.toFloat() / 100
}

fun percentageOf(value: Int, total: Int): Int? {
    return if (total != 0) ((value.toFloat() / total) * 100).roundToInt() else null
}