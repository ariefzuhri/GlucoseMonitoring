package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

private fun formatter(format: DateFormat): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(format.pattern, LOCALE_INDONESIAN)
}

fun LocalDateTime.toString(outputFormat: DateFormat): String {
    return try {
        this.format(formatter(outputFormat))
    } catch (e: Exception) {
        e.printStackTrace()
        String.NONE
    }
}

fun LocalDate.toString(outputFormat: DateFormat): String {
    return try {
        this.format(formatter(outputFormat))
    } catch (e: Exception) {
        e.printStackTrace()
        String.NONE
    }
}

fun LocalTime.toString(outputFormat: DateFormat): String {
    return try {
        this.format(formatter(outputFormat))
    } catch (e: Exception) {
        e.printStackTrace()
        String.NONE
    }
}

// Only accepts the full format (the input format must include date and time)
fun String.toDateTime(inputFormat: DateFormat): LocalDateTime {
    return try {
        LocalDateTime.parse(this, formatter(inputFormat))
    } catch (e: Exception) {
        e.printStackTrace()
        LocalDateTime.of(
            0, 1, 1, 0,
            0, 0, 0,
        )
    }
}

// The input format only accepts the date format
fun String.toDate(inputFormat: DateFormat): LocalDate {
    return try {
        LocalDate.parse(this, formatter(inputFormat))
    } catch (e: Exception) {
        e.printStackTrace()
        LocalDate.of(0, 1, 1)
    }
}

// The input format only accepts the time format
fun String.toTime(inputFormat: DateFormat): LocalTime {
    return try {
        LocalTime.parse(this, formatter(inputFormat))
    } catch (e: Exception) {
        e.printStackTrace()
        LocalTime.of(0, 0, 0, 0)
    }
}

fun LocalDateTime.timeMillis(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun LocalDate.timeMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun timeMillisToDateTime(timeMillis: Long): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeMillis),
        ZoneId.systemDefault(),
    )
}

fun differencesOfDates(d1: LocalDate, d2: LocalDate): Long {
    val diff = d2.timeMillis() - d1.timeMillis()
    return (diff.toDouble() / (1000 * 60 * 60 * 24)).roundToLong()
}

fun getCurrentDateTime(): LocalDateTime {
    // return LocalDateTime.now()
    return Constants.DUMMY_DATE.toDateTime(DateFormat.RAW_FULL)
}

fun getCurrentDate(): LocalDate {
    // return LocalDate.now()
    return Constants.DUMMY_DATE.toDate(DateFormat.RAW_FULL)
}