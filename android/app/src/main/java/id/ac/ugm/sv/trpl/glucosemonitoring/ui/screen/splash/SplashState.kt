package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.splash

data class SplashState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    val isDownloadingData: Boolean = false,
    val enableGlucoseAlarms: Boolean = false,
)