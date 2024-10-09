package id.ac.ugm.sv.trpl.glucosemonitoring.domain.model

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.minus
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventEffectDuration
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import kotlin.math.roundToInt

data class Event(
    val id: Int,
    val type: EventType,
    val desc: String,
    val date: String,
    val time: String,
) {
    
    val dateTime get() = "$date $time"
    
    var relatedGlucoseData: List<Glucose> = emptyList()
    
    fun populateRelatedGlucoseData(allGlucoseData: List<Glucose>) {
        val hoursBefore = 1L
        val hoursAfter = when (type) {
            EventType.MEAL -> EventEffectDuration.MEAL
            EventType.EXERCISE -> EventEffectDuration.EXERCISE
            EventType.MEDICATION -> EventEffectDuration.MEDICATION
        }.maxHours
        
        val formattedDateTime = dateTime.toDateTime(DateFormat.RAW_FULL)
        val startDateTime = formattedDateTime.minusHours(hoursBefore)
        val endDateTime = formattedDateTime.plusHours(hoursAfter)
        val period = startDateTime.toString(DateFormat.RAW_FULL)..
                endDateTime.toString(DateFormat.RAW_FULL)
        
        relatedGlucoseData = allGlucoseData.filter {
            it.dateTime in period
        }
    }
    
    val initialGlucose: Int?
        get() {
            return if (relatedGlucoseData.isNotEmpty()) {
                var initialLevel: Int? = null
                
                // Take the initial glucose level value
                // right before the event time using backward looping
                for (i in relatedGlucoseData.lastIndex downTo 0) {
                    val glucose = relatedGlucoseData[i]
                    // If no glucose is sought, take it right after the event time
                    // (don't put this in the if block)
                    initialLevel = glucose.level.roundToInt()
                    if (glucose.dateTime <= dateTime) break
                }
                initialLevel
            } else {
                null
            }
        }
    
    val finalGlucose: Int?
        get() {
            return if (type == EventType.MEAL) {
                relatedGlucoseData.lastOrNull()?.level?.roundToInt()
            } else {
                null
            }
        }
    
    val glucoseChanges: Int?
        get() {
            return finalGlucose.minus(initialGlucose)
        }
    
}