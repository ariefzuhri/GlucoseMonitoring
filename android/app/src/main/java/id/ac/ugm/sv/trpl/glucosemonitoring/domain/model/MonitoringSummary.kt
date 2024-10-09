package id.ac.ugm.sv.trpl.glucosemonitoring.domain.model

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory

data class MonitoringSummary(
    val latestGlucoseCategory: GlucoseCategory,
    val latestGlucoseLevel: Int?,
    val latestGlucoseTime: String?,
    val latestGlucoseData: List<Glucose>,
    val lastSeenGlucoseChanges: Int?,
    val lastSeenGlucoseTime: String?,
    val lastMeal: Event?,
    val lastExercise: Event?,
    val lastMedication: Event?,
)