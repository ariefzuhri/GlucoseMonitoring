package id.ac.ugm.sv.trpl.glucosemonitoring.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.navigation.Screen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.monitoringdetails.MonitoringDetailsScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MonitoringDetailsScreenTest {
    
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()
    
    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            GlucoverTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.MonitoringDetails.route,
                ) {
                    composable(Screen.MonitoringDetails.route) {
                        MonitoringDetailsScreen({ _, _ -> }, {})
                    }
                }
            }
        }
    }
    
    @Test
    fun showMonitoringDetailsScreen_DisplayCorrectly() {
        composeRule.onNodeWithText("Data 24 jam").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.GLUCOSE_CHART).assertIsDisplayed()
        
        composeRule.onNodeWithTag(TestTags.STATS_LAST_VALUE)
            .assertTextEquals(DummyData.DUMMY_DATE_1_GLUCOSES_LAST_VALUE.toString())
        composeRule.onNodeWithTag(TestTags.STATS_AVERAGE)
            .assertTextEquals(DummyData.DUMMY_DATE_1_GLUCOSES_AVERAGE.toString())
        composeRule.onNodeWithTag(TestTags.STATS_MAX)
            .assertTextEquals(DummyData.DUMMY_DATE_1_GLUCOSES_MAX.toString())
        composeRule.onNodeWithTag(TestTags.STATS_MIN)
            .assertTextEquals(DummyData.DUMMY_DATE_1_GLUCOSES_MIN.toString())
        
        composeRule.onNodeWithTag(TestTags.TIR_PERCENTAGE)
            .assertTextEquals("${DummyData.DUMMY_DATE_1_GLUCOSES_TIR}%")
        
        val expectedRecommendations = Recommendations.Builder()
            .setTimeInRange(
                DummyData.DUMMY_DATE_1_GLUCOSES_TIR,
                DummyData.DUMMY_DATE_1_GLUCOSES_TAR,
                DummyData.DUMMY_DATE_1_GLUCOSES_TBR,
            )
            .setBloodPressure(
                DummyData.HealthNumbers.SYSTOLIC,
                DummyData.HealthNumbers.DIASTOLIC,
            )
            .setBmi(
                DummyData.HealthNumbers.WEIGHT,
                DummyData.HealthNumbers.HEIGHT,
            )
        
        composeRule.onNodeWithTag(TestTags.GLUCOSE_RECOMMENDATION).assertTextEquals(
            expectedRecommendations.glucoseRecommendation!!.getRecommendation()
        )
        composeRule.onNodeWithTag(TestTags.BLOOD_PRESSURE_RECOMMENDATION).assertTextEquals(
            expectedRecommendations.bloodPressureRecommendation!!.getRecommendation()
        )
        composeRule.onNodeWithTag(TestTags.BMI_RECOMMENDATION).assertTextEquals(
            expectedRecommendations.bmiRecommendation!!.getRecommendation()
        )
    }
}