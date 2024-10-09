package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventEffectDuration
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDateTime
import javax.inject.Inject

class GetEventDetailsUseCase @Inject constructor(
    private val eventRepository: IEventRepository,
    private val glucoseRepository: IGlucoseRepository,
) {
    
    operator fun invoke(id: Int): Flowable<Pair<Event, List<Event>>> {
        return glucoseRepository.getGlucoseData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .take(1)
            .flatMap { glucoseData ->
                eventRepository.getEventData().map { eventData ->
                    eventData.filter { it.id == id } to eventData
                }.takeWhile { (selectedEvent, _) ->
                    selectedEvent.isNotEmpty()
                }.map { (selectedEvent, eventData) ->
                    selectedEvent.first() to eventData
                }.map { (selectedEvent, eventData) ->
                    val sortedGlucoseData = glucoseData.sortedBy { it.dateTime }
                    selectedEvent.populateRelatedGlucoseData(allGlucoseData = sortedGlucoseData)
                    
                    val relatedEventData = getRelatedEventData(
                        baseEvent = selectedEvent,
                        allEventData = eventData,
                        allGlucoseData = sortedGlucoseData,
                    ).sortedBy { it.dateTime }
                    
                    selectedEvent to relatedEventData
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
    @Suppress("UnnecessaryVariable")
    private fun getRelatedEventData(
        baseEvent: Event,
        allEventData: List<Event>, allGlucoseData: List<Glucose>,
    ): List<Event> {
        val baseEventEffectDuration = when (baseEvent.type) {
            EventType.MEAL -> EventEffectDuration.MEAL
            EventType.EXERCISE -> EventEffectDuration.EXERCISE
            EventType.MEDICATION -> EventEffectDuration.MEDICATION
        }.maxHours
        
        val baseEventDateTime = baseEvent.dateTime.toDateTime(DateFormat.RAW_FULL)
        val baseEventPeriodStart = baseEventDateTime
        val baseEventPeriodEnd = baseEventDateTime.plusHours(baseEventEffectDuration)
        val baseEventPeriod = baseEventPeriodStart..baseEventPeriodEnd
        
        val relatedEventData = allEventData.filter { anotherEvent ->
            val anotherEventEffectDuration = when (anotherEvent.type) {
                EventType.MEAL -> EventEffectDuration.MEAL
                EventType.EXERCISE -> EventEffectDuration.EXERCISE
                EventType.MEDICATION -> EventEffectDuration.MEDICATION
            }.maxHours
            
            val anotherEventDateTime = anotherEvent.dateTime.toDateTime(DateFormat.RAW_FULL)
            val anotherEventPeriodStart = anotherEventDateTime
            val anotherEventPeriodEnd = anotherEventDateTime.plusHours(anotherEventEffectDuration)
            val anotherEventPeriod = anotherEventPeriodStart..anotherEventPeriodEnd
            
            anotherEvent.id != baseEvent.id && isOverlapping(baseEventPeriod, anotherEventPeriod)
        }
        relatedEventData.forEach {
            it.populateRelatedGlucoseData(allGlucoseData)
        }
        return relatedEventData
    }
    
    private fun isOverlapping(
        firstPeriod: ClosedRange<LocalDateTime>,
        secondPeriod: ClosedRange<LocalDateTime>,
    ): Boolean {
        return firstPeriod.start <= secondPeriod.endInclusive &&
                secondPeriod.start <= firstPeriod.endInclusive
    }
    
}