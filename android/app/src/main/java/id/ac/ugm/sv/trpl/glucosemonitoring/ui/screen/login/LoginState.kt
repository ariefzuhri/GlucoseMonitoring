package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.login

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result

data class LoginState(
    val email: String = String.EMPTY,
    val password: String = String.EMPTY,
    val loginResult: Result<Nothing> = Result.Standby,
)