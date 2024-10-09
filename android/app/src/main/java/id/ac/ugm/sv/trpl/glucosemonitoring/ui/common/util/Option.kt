package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.MonitoringDetailsPeriodFilter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons

@Stable
class Option(
    val name: String,
    @StringRes val displayName: Int,
    @DrawableRes val icon: Int? = null,
    initialIsEnabled: Boolean = false,
) {
    
    val isEnabled = mutableStateOf(initialIsEnabled)
    
}

val eventTypeOptions = listOf(
    Option(
        name = EventType.MEAL.id,
        displayName = R.string.event_type_option_meal,
        initialIsEnabled = true,
    ),
    Option(
        name = EventType.EXERCISE.id,
        displayName = R.string.event_type_option_exercise,
    ),
    Option(
        name = EventType.MEDICATION.id,
        displayName = R.string.event_type_option_medication,
    ),
)

val monitoringDetailsPeriodFilters = listOf(
    Option(
        name = MonitoringDetailsPeriodFilter.LAST_24_HOUR.name,
        displayName = R.string.monitoring_details_period_filter_last_24_hour,
        initialIsEnabled = true,
    ),
    Option(
        name = MonitoringDetailsPeriodFilter.SEVEN_DAYS.name,
        displayName = R.string.monitoring_details_period_filter_seven_days,
    ),
    Option(
        name = MonitoringDetailsPeriodFilter.FOURTEEN_DAYS.name,
        displayName = R.string.monitoring_details_period_filter_fourteen_days,
    ),
    Option(
        name = MonitoringDetailsPeriodFilter.THIRTY_DAYS.name,
        displayName = R.string.monitoring_details_period_filter_thirty_days,
    ),
    Option(
        name = MonitoringDetailsPeriodFilter.NINETY_DAYS.name,
        displayName = R.string.monitoring_details_period_filter_ninety_days,
    ),
    Option(
        name = MonitoringDetailsPeriodFilter.CUSTOM.name,
        displayName = R.string.monitoring_details_period_filter_custom,
        icon = GlucoverIcons.DateRange,
    ),
)