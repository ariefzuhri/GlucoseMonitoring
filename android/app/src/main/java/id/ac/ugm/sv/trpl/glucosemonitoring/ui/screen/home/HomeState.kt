package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.home

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringSummary

data class HomeState(
    val monitoringSummary: MonitoringSummary = MonitoringSummary(
        latestGlucoseCategory = GlucoseCategory.UNKNOWN,
        latestGlucoseLevel = null,
        latestGlucoseTime = null,
        latestGlucoseData = emptyList(),
        lastSeenGlucoseChanges = null,
        lastSeenGlucoseTime = null,
        lastMeal = null,
        lastExercise = null,
        lastMedication = null,
    ),
    val userName: String = String.EMPTY,
)