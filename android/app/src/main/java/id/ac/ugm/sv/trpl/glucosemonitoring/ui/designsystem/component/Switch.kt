package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
fun Switch(
    @StringRes title: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = title.toText(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
        )
        MaterialSwitch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary.copy(0.75f),
                checkedTrackColor = MaterialTheme.colorScheme.surface.copy(0.25f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                uncheckedTrackColor = MaterialTheme.colorScheme.surface.copy(0.25f),
                uncheckedBorderColor = Color.Transparent,
            ),
            modifier = Modifier.testTag(testTag),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitchPreview() {
    GlucoverTheme {
        Switch(
            title = R.string.txt_enable_glucose_alarms_profile,
            isChecked = true,
            onCheckedChange = {},
        )
    }
}