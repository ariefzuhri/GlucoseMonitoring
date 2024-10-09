package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventform

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDate
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.eventTypeOptions
import java.time.LocalDate
import java.time.LocalTime

data class EventFormState(
    val isEdit: Boolean = false,
    val id: Int? = null,
    val type: EventType = EventType.MEAL,
    val desc: String = String.EMPTY,
    val date: LocalDate = getCurrentDate(),
    val time: LocalTime = LocalTime.now(),
    val typeOptions: List<Option> = eventTypeOptions,
    val addResult: Result<Nothing> = Result.Standby,
    val editResult: Result<Nothing> = Result.Standby,
)