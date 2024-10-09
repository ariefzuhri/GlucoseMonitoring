package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action

import android.content.Context
import android.content.Intent
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.disableBroadcastReceiver
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.enableBroadcastReceiver
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.startService
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.receiver.glucosealarms.GlucoseAlarmsReceiver
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.service.glucosealarms.GlucoseAlarmsService

fun Context.enableGlucoseAlarmsReceiver() {
    enableBroadcastReceiver(GlucoseAlarmsReceiver::class.java)
}

fun Context.disableGlucoseAlarmsReceiver() {
    disableBroadcastReceiver(GlucoseAlarmsReceiver::class.java)
}

fun Context.startGlucoseAlarmsService() {
    if (!GlucoseAlarmsService.IS_RUNNING) {
        Intent(this, GlucoseAlarmsService::class.java)
            .also { intent -> startService(this, intent) }
    }
}

fun Context.stopGlucoseAlarmsService() {
    if (GlucoseAlarmsService.IS_RUNNING) {
        Intent(this, GlucoseAlarmsService::class.java)
            .also { intent -> stopService(intent) }
    }
}