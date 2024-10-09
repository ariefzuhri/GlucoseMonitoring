package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockDialog(
    state: UseCaseState,
    time: LocalTime,
    onSelectTime: (LocalTime) -> Unit,
) {
    GlucoverTheme(fixBackgroundColor = true) {
        com.maxkeppeler.sheets.clock.ClockDialog(
            state = state,
            config = ClockConfig(
                defaultTime = time,
                is24HourFormat = true,
            ),
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                onSelectTime(LocalTime.of(hours, minutes))
            },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ClockDialogPreview() {
    GlucoverTheme(fixBackgroundColor = true) {
        val clockDialog = rememberUseCaseState(true)
        ClockDialog(
            state = clockDialog,
            time = LocalTime.now(),
            onSelectTime = {},
        )
    }
}