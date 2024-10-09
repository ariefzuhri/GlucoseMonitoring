package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toPainter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.LoadingIndicator
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun SplashScreen(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    startGlucoseAlarmsService: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    SplashContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToLogin = navigateToLogin,
        navigateToHome = navigateToHome,
        startGlucoseAlarmsService = startGlucoseAlarmsService,
        modifier = modifier,
    )
}

@Composable
private fun SplashContent(
    state: SplashState,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    startGlucoseAlarmsService: () -> Unit,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Image(
            painter = GlucoverIcons.Logotype.toPainter(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(140.dp),
        )
        AnimatedVisibility(visible = state.isDownloadingData) {
            LoadingIndicator(modifier = Modifier.padding(top = 24.dp))
        }
    }
    
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            if (state.isLoggedIn) {
                navigateToHome()
                if (state.enableGlucoseAlarms) startGlucoseAlarmsService()
            } else {
                navigateToLogin()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    GlucoverTheme {
        SplashScreen(
            navigateToLogin = {},
            navigateToHome = {},
            startGlucoseAlarmsService = {},
        )
    }
}