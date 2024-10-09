package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.showToast
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toPainter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.PrimaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.TextField
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun LoginScreen(
    restartApp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LoginContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onInputEmail = viewModel::onInputEmail,
        onInputPassword = viewModel::onInputPassword,
        login = viewModel::login,
        restartApp = restartApp,
        modifier = modifier,
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    onInputEmail: (String) -> Unit,
    onInputPassword: (String) -> Unit,
    login: () -> Unit,
    restartApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        Spacer(modifier = Modifier.height(72.dp))
        
        HeaderSection()
        
        Spacer(modifier = Modifier.height(64.dp))
        
        FormSection(
            email = state.email,
            password = state.password,
            onInputEmail = onInputEmail,
            onInputPassword = onInputPassword,
            login = login,
            isLoading = state.loginResult is Result.Loading,
        )
    }
    
    ResultToast(
        loginResult = state.loginResult,
        restartApp = restartApp,
    )
}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Image(
            painter = GlucoverIcons.Logotype.toPainter(),
            contentDescription = R.string.app_name.toText(),
            contentScale = ContentScale.FillHeight,
            modifier = modifier.height(32.dp),
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = R.string.txt_welcome_login.toText(),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = R.string.txt_desc_login.toText(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun FormSection(
    email: String,
    password: String,
    onInputEmail: (String) -> Unit,
    onInputPassword: (String) -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
) {
    val focusManager = LocalFocusManager.current
    
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        TextField(
            text = email,
            icon = GlucoverIcons.Email,
            placeholder = R.string.edt_placeholder_email_login,
            onTextChange = onInputEmail,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextField(
            text = password,
            icon = GlucoverIcons.Password,
            placeholder = R.string.edt_placeholder_password_login,
            onTextChange = onInputPassword,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(
                onDone = {
                    login()
                    focusManager.clearFocus()
                },
            ),
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        PrimaryButton(
            title = R.string.btn_login_login,
            icon = GlucoverIcons.Login,
            isLoading = isLoading,
            onClick = login,
            modifier = modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ResultToast(
    loginResult: Result<Nothing>,
    restartApp: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(loginResult) {
        when (loginResult) {
            is Result.Success -> {
                restartApp()
            }
            
            is Result.Failed -> {
                context.showToast(R.string.toast_failed_unknown_login)
            }
            
            else -> {}
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    GlucoverTheme {
        LoginScreen(
            restartApp = {},
        )
    }
}