package id.ac.ugm.sv.trpl.glucosemonitoring.domain.model

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseAlarmLevel

data class GlucoseAlarm(
    val alarmLevel: GlucoseAlarmLevel,
    val glucoseLevel: Int,
    val alertWithSound: Boolean,
    val alertWithVibration: Boolean,
)