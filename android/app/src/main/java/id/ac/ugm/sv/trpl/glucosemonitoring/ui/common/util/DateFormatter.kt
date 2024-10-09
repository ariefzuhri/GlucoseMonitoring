package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import android.content.Context
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.differencesOfDates
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDate
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toDate
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat

fun String.toFuzzyDate(
    context: Context,
    inputFormat: DateFormat,
    longDate: Boolean = false,
): String {
    val todayDate = getCurrentDate()
    val inputDate = this.toDate(inputFormat)
    return when (differencesOfDates(todayDate, inputDate)) {
        0L -> context.getString(R.string.today)
        -1L -> context.getString(R.string.yesterday)
        else -> inputDate.toString(
            if (longDate) DateFormat.READABLE_LONG_DATE
            else DateFormat.READABLE_SHORT_DATE
        )
    }
}

fun String.toReadableTime(inputFormat: DateFormat): String {
    return this.toTime(inputFormat).toString(DateFormat.READABLE_TIME)
}