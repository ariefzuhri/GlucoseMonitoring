package id.ac.ugm.sv.trpl.glucosemonitoring.ui.receiver.glucosealarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action.startGlucoseAlarmsService

class GlucoseAlarmsReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startGlucoseAlarmsService()
        }
    }
    
}