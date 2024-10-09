package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

fun Context.enableBroadcastReceiver(broadcastReceiverClass: Class<*>) {
    val packageManager = packageManager
    val componentName = ComponentName(this, broadcastReceiverClass)
    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP,
    )
}

fun Context.disableBroadcastReceiver(broadcastReceiverClass: Class<*>) {
    val packageManager = packageManager
    val componentName = ComponentName(this, broadcastReceiverClass)
    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP,
    )
}

fun startService(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}