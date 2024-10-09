package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import android.util.Range
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.LOCALE_INDONESIAN
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDate
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import java.time.LocalDate

@Composable
fun CalendarDialog(
    state: UseCaseState,
    date: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
) {
    BaseCalendarDialog(
        state = state,
        selection = CalendarSelection.Date(
            selectedDate = date,
            onSelectDate = onSelectDate,
        ),
    )
}

@Composable
fun CalendarDialog(
    state: UseCaseState,
    period: Range<LocalDate>,
    onSelectPeriod: (LocalDate, LocalDate) -> Unit,
) {
    BaseCalendarDialog(
        state = state,
        selection = CalendarSelection.Period(
            selectedRange = period,
            onSelectRange = onSelectPeriod,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseCalendarDialog(
    state: UseCaseState,
    selection: CalendarSelection,
) {
    GlucoverTheme(fixBackgroundColor = true) {
        com.maxkeppeler.sheets.calendar.CalendarDialog(
            state = state,
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
                locale = LOCALE_INDONESIAN,
            ),
            selection = selection,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CalendarDialogPreview() {
    GlucoverTheme(fixBackgroundColor = true) {
        val calendarDialog = rememberUseCaseState(true)
        CalendarDialog(
            state = calendarDialog,
            date = getCurrentDate(),
            onSelectDate = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PeriodCalendarDialogPreview() {
    GlucoverTheme(fixBackgroundColor = true) {
        val calendarDialog = rememberUseCaseState(true)
        CalendarDialog(
            state = calendarDialog,
            period = getCurrentDate().let { Range(it.minusDays(2), it) },
            onSelectPeriod = { _, _ -> },
        )
    }
}