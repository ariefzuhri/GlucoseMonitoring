package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventlist

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event

data class EventListState(
    val eventData: List<Event> = emptyList(),
)