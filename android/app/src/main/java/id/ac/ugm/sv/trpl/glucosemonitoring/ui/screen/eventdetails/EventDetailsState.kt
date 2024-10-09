package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventdetails

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.INVALID
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event

data class EventDetailsState(
    val event: Event = Event(
        Int.INVALID,
        EventType.MEAL,
        String.EMPTY,
        String.EMPTY,
        String.EMPTY,
    ),
    val relatedEventData: List<Event> = emptyList(),
    val deleteResult: Result<Nothing> = Result.Standby,
)