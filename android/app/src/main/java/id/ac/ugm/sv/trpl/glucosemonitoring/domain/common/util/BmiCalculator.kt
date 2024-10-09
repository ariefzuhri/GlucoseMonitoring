package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.WeightStatus
import kotlin.math.roundToInt

// Source: American Diabetes Association (ADA)
fun calculateBmi(weight: Int, height: Int): Float {
    return if (height != 0) {
        val heightMeters = cmToMeters(height)
        val result = weight / (heightMeters * heightMeters)
        "%.1f".format(result).toFloat()
    } else {
        0f
    }
}

fun calculateHealthyWeight(height: Int): IntRange {
    val heightMeters = cmToMeters(height)
    return (heightMeters * heightMeters).let {
        (WeightStatus.NORMAL_WEIGHT.range.start * it).roundToInt()..
                (WeightStatus.NORMAL_WEIGHT.range.endInclusive * it).roundToInt()
    }
}