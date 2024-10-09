package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.minus
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringSummary
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.math.roundToInt

class GetMonitoringSummaryUseCase @Inject constructor(
    private val glucoseRepository: IGlucoseRepository,
    private val eventRepository: IEventRepository,
    private val lastSeenRepository: ILastSeenRepository,
) {
    
    operator fun invoke(): Flowable<MonitoringSummary> {
        val glucoseDataFlowable = glucoseRepository.getGlucoseData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
        val eventDataFlowable = eventRepository.getEventData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
        val lastSeenFlowable = lastSeenRepository.getLastSeen()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .take(1)
        
        return Flowable.combineLatest(
            glucoseDataFlowable,
            eventDataFlowable,
            lastSeenFlowable,
        ) { glucoseData, eventData, lastSeen ->
            val currentDateTime = getCurrentDateTime()
            val startDateTime = currentDateTime.minusHours(24)
            @Suppress("UnnecessaryVariable") val endDateTime = currentDateTime
            val last24HourPeriod = startDateTime.toString(DateFormat.RAW_FULL)..
                    endDateTime.toString(DateFormat.RAW_FULL)
            
            // Last 24-hour glucose records
            val latestGlucoseData = glucoseData.filter {
                it.dateTime in last24HourPeriod
            }.sortedBy { it.dateTime }
            
            val latestGlucoseLevel = latestGlucoseData.lastOrNull()?.level?.roundToInt()
            val latestGlucoseCategory = GlucoseCategory.fromInt(latestGlucoseLevel)
            val latestGlucoseTime = latestGlucoseData.lastOrNull()?.time
            
            // Last seen glucose changes
            val lastSeenGlucoseChanges =
                latestGlucoseLevel.minus(lastSeen.glucoseLevel)
            
            // Latest event logging
            val sortedEventData = eventData.sortedBy { it.dateTime }
            val lastMeal = sortedEventData.findLast { it.type == EventType.MEAL }
            val lastExercise = sortedEventData.findLast { it.type == EventType.EXERCISE }
            val lastMedication = sortedEventData.findLast { it.type == EventType.MEDICATION }
            
            lastMeal?.populateRelatedGlucoseData(allGlucoseData = glucoseData)
            
            MonitoringSummary(
                latestGlucoseCategory = latestGlucoseCategory,
                latestGlucoseLevel = latestGlucoseLevel,
                latestGlucoseTime = latestGlucoseTime,
                latestGlucoseData = latestGlucoseData,
                lastSeenGlucoseChanges = lastSeenGlucoseChanges,
                lastSeenGlucoseTime = lastSeen.glucoseTime,
                lastMeal = lastMeal,
                lastExercise = lastExercise,
                lastMedication = lastMedication,
            )
        }.observeOn(AndroidSchedulers.mainThread())
    }
    
}