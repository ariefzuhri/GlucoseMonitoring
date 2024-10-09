package id.ac.ugm.sv.trpl.glucosemonitoring.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ServiceTestRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.navigation.Screen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.profile.ProfileScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.service.glucosealarms.GlucoseAlarmsService
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class GlucoseAlarmsTest {
    
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()
    
    @get:Rule(order = 2)
    val serviceRule = ServiceTestRule()
    
    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            GlucoverTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Profile.route,
                ) {
                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            navigateToHealthNumbersForm = {},
                            navigateToLogin = {},
                            onBack = {},
                        )
                    }
                }
            }
        }
    }
    
    @Test
    fun enableGlucoseAlarms_ServiceIsStarted() {
        assertThat(GlucoseAlarmsService.IS_RUNNING).isFalse()
        
        val context = ApplicationProvider.getApplicationContext<Context>()
        val serviceIntent = Intent(
            context,
            GlucoseAlarmsService::class.java,
        )
        val binder = serviceRule.bindService(serviceIntent)
        val service = (binder as GlucoseAlarmsService.LocalBinder).getService()

        context.startService(serviceIntent)
        composeRule.onNodeWithTag(TestTags.GLUCOSE_ALARMS_SWITCH).performClick()
        assertThat(GlucoseAlarmsService.IS_RUNNING).isTrue()
    }
}