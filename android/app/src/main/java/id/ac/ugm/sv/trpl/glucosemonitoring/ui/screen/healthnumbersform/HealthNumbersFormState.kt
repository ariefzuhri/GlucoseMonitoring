package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.healthnumbersform

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY

data class HealthNumbersFormState(
    val weight: String = String.EMPTY,
    val height: String = String.EMPTY,
    val systolic: String = String.EMPTY,
    val diastolic: String = String.EMPTY,
)