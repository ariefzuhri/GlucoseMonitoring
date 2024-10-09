package id.ac.ugm.sv.trpl.glucosemonitoring.ui

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Constants
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.navigation.Screen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventdetails.EventDetailsScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventform.EventFormScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventlist.EventListScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EventLoggingTest {
    
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
                    startDestination = Screen.EventList.route,
                ) {
                    composable(Screen.EventList.route) {
                        EventListScreen(
                            navigateToEventDetails = { event ->
                                navController.navigate(Screen.EventDetails.createRoute(event.id))
                            },
                            navigateToAddEventForm = {
                                navController.navigate(Screen.AddEventForm.route)
                            },
                            onBack = {},
                        )
                    }
                    composable(Screen.AddEventForm.route) {
                        EventFormScreen(
                            onBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                    composable(
                        route = Screen.EventDetails.route,
                        arguments = listOf(navArgument(Constants.EXTRAS.EVENT_ID) {
                            type = NavType.IntType
                        }),
                    ) {
                        val eventId = it.arguments?.getInt(Constants.EXTRAS.EVENT_ID)
                        eventId?.let { id ->
                            EventDetailsScreen(
                                id = id,
                                navigateToEventDetails = {},
                                navigateToEditEventForm = {},
                                navigateToExpandedChart = { _, _ -> },
                                onBack = {
                                    navController.popBackStack()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
    
    @Test
    fun logEvent_WorkCorrectly() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        
        // Navigate to add event screen
        composeRule.onNodeWithText(context.getString(R.string.btn_add_event_eventlist))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.txt_top_title_add_eventform))
            .assertIsDisplayed()
        
        // Fill the form
        composeRule.onNodeWithText(context.getString(R.string.event_type_option_meal))
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.edt_placeholder_desc_meal_eventform))
            .performTextInput("Nasi goreng")
        
        composeRule.onNodeWithTag(TestTags.INPUT_TIME).performClick()
        composeRule.onNodeWithText("0").performClick()
        composeRule.onAllNodesWithText("7").onLast().performClick()
        composeRule.onNodeWithText("0").performClick()
        composeRule.onNodeWithText("0").performClick()
        composeRule.onAllNodesWithText("OK").onLast().performClick()
        composeRule.onNodeWithText("OK").performClick()
        
        // Save the event and navigate back to event list screen
        composeRule.onNodeWithText(context.getString(R.string.btn_save_eventform))
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithText(context.getString(R.string.txt_top_title_eventlist))
            .assertIsDisplayed()
        
        // Check if the event is saved and navigate to event details screen
        composeRule.onNodeWithTag("EventListContent")
            .performScrollToNode(hasText("Makan nasi goreng"))
        composeRule.onNodeWithText("Makan nasi goreng")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        
        // Check if the event details screen is displayed correctly
        composeRule.onNodeWithText("Makan").assertIsDisplayed()
        composeRule.onAllNodesWithText("Hari ini, 07.00").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Nasi goreng").assertIsDisplayed()
        
        composeRule.onNodeWithText("60").assertIsDisplayed()
        composeRule.onNodeWithText("100").assertIsDisplayed()
        composeRule.onNodeWithText("+40").assertIsDisplayed()
        
        composeRule.onNodeWithTag(TestTags.GLUCOSE_CHART).assertIsDisplayed()
        
        // Related event
        // Meal
        composeRule.onAllNodesWithText(DummyData.event1.desc).onFirst()
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onAllNodesWithText("Hari ini, 07.00").onLast().assertIsDisplayed()
        // Exercise
        composeRule.onAllNodesWithText(DummyData.event2.desc).onLast()
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Hari ini, 08.00").performClick()
    }
}