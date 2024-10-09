package id.ac.ugm.sv.trpl.glucosemonitoring.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action.startGlucoseAlarmsService
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Constants
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventdetails.EventDetailsScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventform.EventFormScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventlist.EventListScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.expandedchart.ExpandedChartScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.home.HomeScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.login.LoginScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.monitoringdetails.MonitoringDetailsScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.healthnumbersform.HealthNumbersFormScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.profile.ProfileScreen
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login.route)
                },
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Screen.Home.route)
                },
                startGlucoseAlarmsService = {
                    context.startGlucoseAlarmsService()
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                restartApp = {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                navigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                navigateToMonitoringDetails = {
                    navController.navigate(Screen.MonitoringDetails.route)
                },
                navigateToAddEventForm = {
                    navController.navigate(Screen.AddEventForm.route)
                },
                navigateToEventDetails = { event ->
                    navController.navigate(Screen.EventDetails.createRoute(event.id))
                },
                navigateToEventList = {
                    navController.navigate(Screen.EventList.route)
                },
                navigateToExpandedChart = { glucoseData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = Constants.EXTRAS.GLUCOSES_LIST,
                        value = glucoseData
                    )
                    navController.navigate(Screen.ExpandedChart.route)
                },
            )
        }
        composable(Screen.MonitoringDetails.route) {
            MonitoringDetailsScreen(
                navigateToExpandedChart = { glucoseData, eventData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = Constants.EXTRAS.GLUCOSES_LIST,
                        value = glucoseData
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = Constants.EXTRAS.EVENT_LIST,
                        value = eventData
                    )
                    navController.navigate(Screen.ExpandedChart.route)
                },
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(Screen.EventList.route) {
            EventListScreen(
                navigateToEventDetails = { event ->
                    navController.navigate(Screen.EventDetails.createRoute(event.id))
                },
                navigateToAddEventForm = {
                    navController.navigate(Screen.AddEventForm.route)
                },
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Screen.EventDetails.route,
            arguments = listOf(navArgument(Constants.EXTRAS.EVENT_ID) { type = NavType.IntType }),
        ) {
            val eventId = it.arguments?.getInt(Constants.EXTRAS.EVENT_ID)
            eventId?.let { id ->
                EventDetailsScreen(
                    id = id,
                    navigateToEventDetails = { event ->
                        navController.navigate(Screen.EventDetails.createRoute(event.id))
                    },
                    navigateToEditEventForm = { event ->
                        navController.navigate(Screen.EditEventForm.createRoute(event.id))
                    },
                    navigateToExpandedChart = { glucoseData, eventData ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = Constants.EXTRAS.GLUCOSES_LIST,
                            value = glucoseData
                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = Constants.EXTRAS.EVENT_LIST,
                            value = eventData
                        )
                        navController.navigate(Screen.ExpandedChart.route)
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
        composable(Screen.AddEventForm.route) {
            EventFormScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Screen.EditEventForm.route,
            arguments = listOf(navArgument(Constants.EXTRAS.EVENT_ID) { type = NavType.IntType }),
        ) {
            val id = it.arguments?.getInt(Constants.EXTRAS.EVENT_ID)
            EventFormScreen(
                id = id,
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                navigateToHealthNumbersForm = {
                    navController.navigate(Screen.HealthNumbersForm.route)
                },
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(Screen.HealthNumbersForm.route) {
            HealthNumbersFormScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(Screen.ExpandedChart.route) {
            val glucoseData =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<Glucose>>(
                    Constants.EXTRAS.GLUCOSES_LIST
                ).orEmpty()
            val eventData =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<Event>>(
                    Constants.EXTRAS.EVENT_LIST
                ).orEmpty()
            ExpandedChartScreen(
                glucoseData = glucoseData,
                eventData = eventData,
            )
        }
    }
}