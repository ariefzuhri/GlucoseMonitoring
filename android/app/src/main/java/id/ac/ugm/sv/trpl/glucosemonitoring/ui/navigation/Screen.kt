package id.ac.ugm.sv.trpl.glucosemonitoring.ui.navigation

import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Constants

sealed class Screen(val route: String) {
    
    object Splash : Screen("splash")
    
    object Login : Screen("login")
    
    object Home : Screen("home")
    
    object MonitoringDetails : Screen("monitoring_details")
    
    object EventList : Screen("event_list")
    
    object EventDetails : Screen("event_details/{${Constants.EXTRAS.EVENT_ID}}") {
        fun createRoute(id: Int) = "event_details/${id}"
    }
    
    object AddEventForm : Screen("event_form")
    
    object EditEventForm : Screen("event_form/{${Constants.EXTRAS.EVENT_ID}}") {
        fun createRoute(id: Int? = null) = "event_form/${id}"
    }
    
    object Profile : Screen("profile")
    
    object HealthNumbersForm : Screen("health_numbers_form")
    
    object ExpandedChart : Screen("expanded_chart")
    
}