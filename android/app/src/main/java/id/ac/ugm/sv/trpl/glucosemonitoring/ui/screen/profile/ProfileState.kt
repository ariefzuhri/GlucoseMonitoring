package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.profile

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings

data class ProfileState(
    val name: String = String.EMPTY,
    val email: String = String.EMPTY,
    val healthNumbers: HealthNumbers = HealthNumbers(null, null, null, null),
    val settings: Settings = Settings(false),
)