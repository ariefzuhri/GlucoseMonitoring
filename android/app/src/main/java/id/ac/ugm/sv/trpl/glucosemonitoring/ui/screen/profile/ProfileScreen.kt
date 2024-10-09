package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.orZero
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action.disableGlucoseAlarmsReceiver
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action.enableGlucoseAlarmsReceiver
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action.startGlucoseAlarmsService
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.action.stopGlucoseAlarmsService
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.showToast
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ActionBar
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Divider
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Icon
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SecondaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Switch
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.TextIcon
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun ProfileScreen(
    navigateToHealthNumbersForm: () -> Unit,
    navigateToLogin: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val onEnableGlucoseAlarmsChange = { isEnabled: Boolean ->
        viewModel.onEnableGlucoseAlarmsChange(isEnabled)
        if (isEnabled) {
            context.startGlucoseAlarmsService()
            context.enableGlucoseAlarmsReceiver()
        } else {
            context.stopGlucoseAlarmsService()
            context.disableGlucoseAlarmsReceiver()
        }
    }
    
    ProfileContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onEnableGlucoseAlarmsChange = onEnableGlucoseAlarmsChange,
        navigateToHealthNumbersForm = navigateToHealthNumbersForm,
        onBack = onBack,
        logout = viewModel::logout,
        navigateToLogin = navigateToLogin,
        modifier = modifier,
    )
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    onEnableGlucoseAlarmsChange: (Boolean) -> Unit,
    navigateToHealthNumbersForm: () -> Unit,
    logout: () -> Unit,
    navigateToLogin: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        ActionBar(action = onBack)
        
        Spacer(modifier = Modifier.height(72.dp))
        
        ProfileSection(
            name = state.name,
            email = state.email,
        )
        
        Divider(modifier = Modifier.padding(vertical = 32.dp))
        
        HealthNumbersSection(
            weight = state.healthNumbers.weight.orZero(),
            height = state.healthNumbers.height.orZero(),
            systolic = state.healthNumbers.systolic.orZero(),
            diastolic = state.healthNumbers.diastolic.orZero(),
            navigateToHealthNumbersForm = navigateToHealthNumbersForm,
        )
        
        Divider(modifier = Modifier.padding(vertical = 32.dp))
        
        SettingsSection(
            settings = state.settings,
            onEnableGlucoseAlarmsChange = onEnableGlucoseAlarmsChange,
        )
        
        Divider(modifier = Modifier.padding(vertical = 32.dp))
        
        AccountSection(
            logout = logout,
            navigateToLogin = navigateToLogin,
        )
    }
}

@Composable
private fun ProfileSection(
    name: String,
    email: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Icon(
            icon = GlucoverIcons.Profile,
            size = 48.dp,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HealthNumbersSection(
    weight: Int,
    height: Int,
    systolic: Int,
    diastolic: Int,
    navigateToHealthNumbersForm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = R.string.txt_health_numbers_profile.toText(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                modifier = Modifier.weight(1f),
            )
            SecondaryButton(
                title = R.string.btn_update_health_numbers_profile,
                icon = GlucoverIcons.HealthNumbersForm,
                onClick = navigateToHealthNumbersForm,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextIcon(
                text = "$weight ${R.string.weight_unit.toText()}",
                icon = GlucoverIcons.BodyWeight,
            )
            TextIcon(
                text = "$height ${R.string.height_unit.toText()}",
                icon = GlucoverIcons.BodyHeight,
            )
            TextIcon(
                text = "$systolic/$diastolic ${R.string.blood_pressure_unit.toText()}",
                icon = GlucoverIcons.BloodPressure,
            )
        }
    }
}

@Composable
private fun SettingsSection(
    settings: Settings,
    onEnableGlucoseAlarmsChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onEnableGlucoseAlarmsChange(isGranted)
        if (!isGranted) {
            context.showToast(R.string.toast_failed_notification_permission_profile)
        }
    }
    
    Column(
        modifier = modifier,
    ) {
        Text(
            text = R.string.txt_settings_profile.toText(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Switch(
            title = R.string.txt_enable_glucose_alarms_profile,
            isChecked = settings.enableGlucoseAlarms,
            onCheckedChange = { isEnabled ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    isEnabled &&
                    !hasNotificationPermission(context)
                ) {
                    checkAndRequestNotificationPermission(context, requestPermissionLauncher)
                } else {
                    onEnableGlucoseAlarmsChange(isEnabled)
                }
            },
            testTag = TestTags.GLUCOSE_ALARMS_SWITCH,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun checkAndRequestNotificationPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
) {
    val permission = Manifest.permission.POST_NOTIFICATIONS
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
    if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
        launcher.launch(permission)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun hasNotificationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
}

@Composable
private fun AccountSection(
    logout: () -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier,
    ) {
        Text(
            text = R.string.txt_account_profile.toText(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
        )
        Spacer(modifier = Modifier.height(32.dp))
        SecondaryButton(
            title = R.string.btn_logout_profile,
            icon = GlucoverIcons.Logout,
            onClick = {
                context.stopGlucoseAlarmsService()
                context.disableGlucoseAlarmsReceiver()
                navigateToLogin()
                logout()
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    GlucoverTheme {
        ProfileScreen(
            navigateToHealthNumbersForm = {},
            navigateToLogin = {},
            onBack = {},
        )
    }
}