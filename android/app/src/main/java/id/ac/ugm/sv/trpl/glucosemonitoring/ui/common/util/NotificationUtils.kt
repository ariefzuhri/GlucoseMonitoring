package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons

fun Notification.show(context: Context, id: Int) {
    @Suppress("MissingPermission")
    NotificationManagerCompat.from(context).notify(id, this)
}

fun createNotification(
    context: Context,
    @StringRes channelId: Int,
    @StringRes channelName: Int,
    @StringRes channelDescription: Int,
    title: String,
    content: String,
    contentIntent: PendingIntent? = null,
    enableSound: Boolean = false,
    enableVibration: Boolean = false,
): Notification {
    val builder = NotificationCompat.Builder(context, context.getString(channelId))
    val notification = builder
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(GlucoverIcons.Notification)
        .setContentIntent(contentIntent)
        .setSilent(!enableSound)
        .setSound(
            if (enableSound) RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            else null
        )
        .setVibrate(
            if (enableVibration) longArrayOf(1000, 1000, 1000, 1000, 1000)
            else null
        )
        .setOngoing(true)
        .build()
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createChannel(
            context = context,
            channelId = channelId,
            channelName = channelName,
            channelDescription = channelDescription,
        )
    }
    
    return notification
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createChannel(
    context: Context,
    @StringRes channelId: Int,
    @StringRes channelName: Int,
    @StringRes channelDescription: Int,
    importanceLevel: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        NotificationManager.IMPORTANCE_HIGH
    } else {
        @Suppress("DEPRECATION")
        Notification.PRIORITY_MAX
    },
) {
    val channel = NotificationChannel(
        context.getString(channelId),
        context.getString(channelName),
        importanceLevel
    ).apply {
        description = context.getString(channelDescription)
    }
    
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}