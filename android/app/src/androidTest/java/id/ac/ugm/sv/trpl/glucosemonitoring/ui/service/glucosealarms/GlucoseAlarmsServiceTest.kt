package id.ac.ugm.sv.trpl.glucosemonitoring.ui.service.glucosealarms

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ServiceTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseAlarmLevel
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.GlucoseAlarm
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.ShowGlucoseAlarmUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class GlucoseAlarmsServiceTest {
    
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val serviceRule = ServiceTestRule()
    
    private lateinit var showGlucoseAlarmUseCase: ShowGlucoseAlarmUseCase
    private lateinit var glucoseAlarmsService: GlucoseAlarmsService
    
    @Before
    fun setUp() {
        hiltRule.inject()
        
        showGlucoseAlarmUseCase = mockk()
        
        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext(),
            GlucoseAlarmsService::class.java
        )
        serviceRule.startService(serviceIntent)
    }
    
    @Test
    fun checkIfNotifyUserCalled() {
        val dummyGlucoseAlarm = GlucoseAlarm(
            alarmLevel = GlucoseAlarmLevel.NORMAL,
            glucoseLevel = 100,
            alertWithSound = false,
            alertWithVibration = false,
        )
        every { showGlucoseAlarmUseCase() } returns Flowable.just(
            dummyGlucoseAlarm
        )
        
        verify {
            showGlucoseAlarmUseCase()
            glucoseAlarmsService["notifyUser"](eq(dummyGlucoseAlarm))
        }
    }
    
}