package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.NONE

fun Any?.spacedPlus(other: Any?, separateWithCommas: Boolean = false): String {
    return if (separateWithCommas) "$this, $other"
    else "$this $other"
}

fun displayGlucoseChanges(value: Int?): String {
    return if (value != null) {
        if (value >= 0) "+$value"
        else value.toString()
    } else {
        String.NONE
    }
}