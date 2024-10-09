package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.averageOfOrNull
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.percentageOf
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.MonitoringDetailsPeriodFilter
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.GlucoseStats
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.Tir
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

class GetMonitoringDetailsUseCase @Inject constructor(
    private val glucoseRepository: IGlucoseRepository,
    private val eventRepository: IEventRepository,
    private val healthNumbersRepository: IHealthNumbersRepository,
) {
    
    operator fun invoke(
        selectedPeriodFilter: MonitoringDetailsPeriodFilter,
        customPeriod: ClosedRange<LocalDate>? = null,
    ): Flowable<MonitoringDetails> {
        val glucoseDataFlowable = glucoseRepository.getGlucoseData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .take(1)
        val eventDataFlowable = eventRepository.getEventData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .take(1)
        val healthNumbersFlowable = healthNumbersRepository.getHealthNumbers()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .take(1)
        
        return Flowable.zip(
            glucoseDataFlowable,
            eventDataFlowable,
            healthNumbersFlowable,
        ) { glucoseData, eventData, healthNumbers ->
            val currentDateTime = getCurrentDateTime()
            val period = when (selectedPeriodFilter) {
                MonitoringDetailsPeriodFilter.LAST_24_HOUR -> {
                    currentDateTime.minusHours(24).toString(DateFormat.RAW_FULL)..
                            currentDateTime.toString(DateFormat.RAW_FULL)
                }
                
                MonitoringDetailsPeriodFilter.SEVEN_DAYS -> {
                    currentDateTime.minusDays(6).toString(DateFormat.RAW_DATE)..
                            currentDateTime.toString(DateFormat.RAW_DATE)
                }
                
                MonitoringDetailsPeriodFilter.FOURTEEN_DAYS -> {
                    currentDateTime.minusDays(13).toString(DateFormat.RAW_DATE)..
                            currentDateTime.toString(DateFormat.RAW_DATE)
                }
                
                MonitoringDetailsPeriodFilter.THIRTY_DAYS -> {
                    currentDateTime.minusDays(29).toString(DateFormat.RAW_DATE)..
                            currentDateTime.toString(DateFormat.RAW_DATE)
                }
                
                MonitoringDetailsPeriodFilter.NINETY_DAYS -> {
                    currentDateTime.minusDays(89).toString(DateFormat.RAW_DATE)..
                            currentDateTime.toString(DateFormat.RAW_DATE)
                }
                
                MonitoringDetailsPeriodFilter.CUSTOM -> {
                    customPeriod!!.start.toString(DateFormat.RAW_DATE)..
                            customPeriod.endInclusive.toString(DateFormat.RAW_DATE)
                }
            }
            
            val glucoseDataByPeriod = glucoseData.filter {
                if (selectedPeriodFilter == MonitoringDetailsPeriodFilter.LAST_24_HOUR) {
                    it.dateTime in period
                } else {
                    it.date in period
                }
            }.sortedBy { it.dateTime }
            val eventDataByPeriod = eventData.filter {
                if (selectedPeriodFilter == MonitoringDetailsPeriodFilter.LAST_24_HOUR) {
                    it.dateTime in period
                } else {
                    it.date in period
                }
            }.sortedBy { it.dateTime }
            
            // Populate related glucose data to get the events initial glucose level
            eventDataByPeriod.forEach { event ->
                event.populateRelatedGlucoseData(allGlucoseData = glucoseData)
            }
            
            val glucoseStats = calculateGlucoseStats(glucoseDataByPeriod)
            val tir = calculateTir(glucoseDataByPeriod)
            val recommendations = provideRecommendations(healthNumbers, tir)
            
            MonitoringDetails(
                glucoseData = glucoseDataByPeriod,
                eventData = eventDataByPeriod,
                glucoseStats = glucoseStats,
                tir = tir,
                recommendations = recommendations,
            )
        }.observeOn(AndroidSchedulers.mainThread())
    }
    
    private fun calculateGlucoseStats(glucoseData: List<Glucose>): GlucoseStats {
        return GlucoseStats(
            lastValue = glucoseData.lastOrNull()?.level?.roundToInt(),
            average = glucoseData.averageOfOrNull { it.level }?.roundToInt(),
            max = glucoseData.maxOfOrNull { it.level }?.roundToInt(),
            min = glucoseData.minOfOrNull { it.level }?.roundToInt(),
        )
    }
    
    private fun calculateTir(glucoseData: List<Glucose>): Tir {
        val glucosesDataCount = glucoseData.size
        var (tirCount, tarCount, tbrCount) = Triple(0, 0, 0)
        glucoseData.forEach { glucose ->
            if (GlucoseCategory.IN_RANGE.range.contains(glucose.level.roundToInt())) tirCount++
            else if (glucose.level.roundToInt() > GlucoseCategory.IN_RANGE.range.last) tarCount++
            else if (glucose.level.roundToInt() < GlucoseCategory.IN_RANGE.range.first) tbrCount++
        }
        
        return Tir(
            timeInRange = percentageOf(tirCount, glucosesDataCount),
            timeAboveRange = percentageOf(tarCount, glucosesDataCount),
            timeBelowRange = percentageOf(tbrCount, glucosesDataCount),
        )
    }
    
    private fun provideRecommendations(
        healthNumbers: HealthNumbers,
        tir: Tir,
    ): Recommendations {
        return Recommendations.Builder()
            .setTimeInRange(tir.timeInRange, tir.timeAboveRange, tir.timeBelowRange)
            .setBloodPressure(healthNumbers.systolic, healthNumbers.diastolic)
            .setBmi(healthNumbers.weight, healthNumbers.height)
            .provide()
    }
    
}