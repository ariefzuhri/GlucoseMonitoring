package id.ac.ugm.sv.trpl.glucosemonitoring.domain.model

data class MonitoringDetails(
    val glucoseData: List<Glucose>,
    val eventData: List<Event>,
    val glucoseStats: GlucoseStats,
    val tir: Tir,
    val recommendations: Recommendations,
) {
    
    data class GlucoseStats(
        val lastValue: Int?,
        val average: Int?,
        val max: Int?,
        val min: Int?,
    )
    
    data class Tir(
        val timeInRange: Int?,
        val timeAboveRange: Int?,
        val timeBelowRange: Int?,
    )
    
}