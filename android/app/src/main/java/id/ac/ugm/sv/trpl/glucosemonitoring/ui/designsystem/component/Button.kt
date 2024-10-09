package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.Typography

@Composable
private fun BasicButton(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    colors: ButtonColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    iconColor: Color = MaterialTheme.colorScheme.primary.copy(0.75f),
) {
    Button(
        shape = CircleShape,
        colors = colors,
        enabled = isEnabled,
        onClick = if (!isLoading) onClick else ({}),
        modifier = modifier,
    ) {
        if (!isLoading) {
            Text(
                text = stringResource(title),
                style = Typography.labelMedium,
            )
            Spacer(modifier = Modifier.size(6.dp))
            Icon(
                icon = icon,
                size = 16.dp,
                color = iconColor,
            )
        } else {
            CircularProgressIndicator(
                color = iconColor,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
fun PrimaryButton(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    BasicButton(
        title = title,
        icon = icon,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        iconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.75f),
        isLoading = isLoading,
        isEnabled = isEnabled,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
fun SecondaryButton(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    BasicButton(
        title = title,
        icon = icon,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        isLoading = isLoading,
        isEnabled = isEnabled,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
fun DangerButton(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    BasicButton(
        title = title,
        icon = icon,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.error,
        ),
        iconColor = MaterialTheme.colorScheme.error.copy(0.75f),
        isLoading = isLoading,
        isEnabled = isEnabled,
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    GlucoverTheme {
        PrimaryButton(
            title = R.string.btn_add_event_home,
            icon = GlucoverIcons.Add,
            onClick = {},
            isLoading = true,
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    GlucoverTheme {
        SecondaryButton(
            title = R.string.btn_details_home,
            icon = GlucoverIcons.Next,
            onClick = {},
        )
    }
}