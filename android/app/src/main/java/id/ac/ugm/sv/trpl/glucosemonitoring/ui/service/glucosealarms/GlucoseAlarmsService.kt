package id.ac.ugm.sv.trpl.glucosemonitoring.ui.service.glucosealarms

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseAlarmLevel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.GlucoseAlarm
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.ShowGlucoseAlarmUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.MainActivity
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.createNotification
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.show
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class GlucoseAlarmsService : Service() {
    
    private val binder = LocalBinder()
    
    class LocalBinder : Binder() {
        
        fun getService(): GlucoseAlarmsService {
            return this.getService()
        }
        
    }
    
    @Inject
    lateinit var showGlucoseAlarmUseCase: ShowGlucoseAlarmUseCase
    
    private val disposable = CompositeDisposable()
    
    private val notificationID = UUID.randomUUID().hashCode()
    
    companion object {
        var IS_RUNNING = false
    }
    
    override fun onCreate() {
        super.onCreate()
        IS_RUNNING = true
        startService()
        monitorRecentGlucoseData()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    private fun startService() {
        startForeground(
            notificationID,
            createNotification(
                context = this,
                channelId = R.string.notif_channel_id_glucosealarms,
                channelName = R.string.notif_channel_name_glucosealarms,
                channelDescription = R.string.notif_channel_description_glucosealarms,
                title = getString(R.string.notif_title_starting_glucosealarms),
                content = getString(R.string.notif_content_starting_glucosealarms),
                contentIntent = provideNotificationContentIntent(),
            )
        )
    }
    
    private fun monitorRecentGlucoseData() {
        disposable.add(
            showGlucoseAlarmUseCase().subscribe { glucoseAlert ->
                notifyUser(glucoseAlert)
            }
        )
    }
    
    private fun notifyUser(glucoseAlarm: GlucoseAlarm) {
        createNotification(
            context = this,
            channelId = R.string.notif_channel_id_glucosealarms,
            channelName = R.string.notif_channel_name_glucosealarms,
            channelDescription = R.string.notif_channel_description_glucosealarms,
            title = "${glucoseAlarm.glucoseLevel} ${getString(R.string.glucose_unit)}",
            content = getString(
                when (glucoseAlarm.alarmLevel) {
                    GlucoseAlarmLevel.DANGEROUSLY_HIGH -> R.string.notif_content_dangerously_high_glucosealarms
                    GlucoseAlarmLevel.HIGH -> R.string.notif_content_high_glucosealarms
                    GlucoseAlarmLevel.TOWARDS_HIGH -> R.string.notif_content_towards_high_glucosealarms
                    GlucoseAlarmLevel.NORMAL -> R.string.notif_content_normal_glucosealarms
                    GlucoseAlarmLevel.TOWARDS_LOW -> R.string.notif_content_towards_low_glucosealarms
                    GlucoseAlarmLevel.LOW -> R.string.notif_content_low_glucosealarms
                    GlucoseAlarmLevel.DANGEROUSLY_LOW -> R.string.notif_content_dangerously_low_glucosealarms
                }
            ),
            contentIntent = provideNotificationContentIntent(),
            enableSound = glucoseAlarm.alertWithSound,
            enableVibration = glucoseAlarm.alertWithVibration,
        ).show(this, notificationID)
    }
    
    private fun provideNotificationContentIntent(): PendingIntent {
        return Intent(this, MainActivity::class.java)
            .let { notificationIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
    }
    
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
    
    override fun onDestroy() {
        super.onDestroy()
        IS_RUNNING = false
        disposable.dispose()
    }
    
}