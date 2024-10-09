package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.monitoringdetails

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDate
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.GlucoseStats
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.Tir
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.monitoringDetailsPeriodFilters
import java.time.LocalDate

data class MonitoringDetailsState(
    val periodFilters: List<Option> = monitoringDetailsPeriodFilters,
    val customPeriod: ClosedRange<LocalDate> = getCurrentDate().let {
        it.minusDays(2)..it
    },
    val glucoseData: List<Glucose> = emptyList(),
    val eventData: List<Event> = emptyList(),
    val glucoseStats: GlucoseStats = GlucoseStats(null, null, null, null),
    val tir: Tir = Tir(null, null, null),
    val recommendations: Recommendations = Recommendations.Builder().provide(),
) {
    
    val selectedPeriodFilter: Option
        get() = periodFilters.find { it.isEnabled.value }!!
    
}