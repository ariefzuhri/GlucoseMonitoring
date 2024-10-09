package id.ac.ugm.sv.trpl.glucosemonitoring.usecase

import com.google.common.truth.Truth.assertThat
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.MonitoringDetailsPeriodFilter
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.GlucoseStats
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.Tir
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetMonitoringDetailsUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import id.ac.ugm.sv.trpl.glucosemonitoring.util.Constants
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class GetMonitoringDetailsUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getMonitoringDetailsUseCase: GetMonitoringDetailsUseCase
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    private lateinit var fakeEventRepository: FakeEventRepository
    private lateinit var fakePersonalDataRepository: FakeHealthNumbersRepository
    
    @Before
    fun setUp() {
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        fakeEventRepository = spyk(FakeEventRepository())
        fakePersonalDataRepository = spyk(FakeHealthNumbersRepository())
        
        getMonitoringDetailsUseCase = spyk(
            GetMonitoringDetailsUseCase(
                glucoseRepository = fakeGlucoseRepository,
                eventRepository = fakeEventRepository,
                healthNumbersRepository = fakePersonalDataRepository,
            ),
            recordPrivateCalls = true,
        )
        
        mockkStatic(::getCurrentDateTime)
        every { getCurrentDateTime() } returns LocalDateTime.of(
            Constants.DummyDate1.YEAR, Constants.DummyDate1.MONTH, Constants.DummyDate1.DAY,
            23, 59, 59, 999
        )
    }
    
    @Test
    fun `Get details, correct glucose data`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
        }
        
        val expectedGlucoseData = DummyData.dummyDate1Glucoses
        val actualGlucoseData = details.glucoseData
        assertThat(actualGlucoseData)
            .containsExactlyElementsIn(expectedGlucoseData)
            .inOrder()
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get details, correct event data`() {
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedEventData = DummyData.dummyDate1Events
        val actualEventData = details.eventData
        assertThat(actualEventData)
            .containsExactlyElementsIn(expectedEventData)
            .inOrder()
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get details, correct glucose stats`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
        }
        
        val expectedGlucoseStats = GlucoseStats(
            lastValue = DummyData.DUMMY_DATE_1_GLUCOSES_LAST_VALUE,
            average = DummyData.DUMMY_DATE_1_GLUCOSES_AVERAGE,
            max = DummyData.DUMMY_DATE_1_GLUCOSES_MAX,
            min = DummyData.DUMMY_DATE_1_GLUCOSES_MIN,
        )
        val actualGlucoseStats = details.glucoseStats
        assertThat(actualGlucoseStats)
            .isEqualTo(expectedGlucoseStats)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get details, correct time in range`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
        }
        
        val expectedTir = Tir(
            timeInRange = DummyData.DUMMY_DATE_1_GLUCOSES_TIR,
            timeAboveRange = DummyData.DUMMY_DATE_1_GLUCOSES_TAR,
            timeBelowRange = DummyData.DUMMY_DATE_1_GLUCOSES_TBR,
        )
        val actualTir = details.tir
        assertThat(actualTir)
            .isEqualTo(expectedTir)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get details, given TIR meets target, return normoglycemia recommendation as the glucose recommendation`() {
        every { getMonitoringDetailsUseCase["calculateTir"](any<List<Glucose>>()) } returns Tir(
            timeInRange = 100,
            timeAboveRange = 0,
            timeBelowRange = 0,
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            getMonitoringDetailsUseCase["calculateTir"](any<List<Glucose>>())
        }
        
        val expectedGlucoseRecommendation =
            Recommendations.NormoglycemiaRecommendation::class.java
        val actualGlucoseRecommendation =
            details.recommendations.glucoseRecommendation
        assertThat(actualGlucoseRecommendation)
            .isInstanceOf(expectedGlucoseRecommendation)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get details, given TIR below target and TAR dominates, return hyperglycemia recommendation as the glucose recommendation`() {
        every { getMonitoringDetailsUseCase["calculateTir"](any<List<Glucose>>()) } returns Tir(
            timeInRange = 0,
            timeAboveRange = 100,
            timeBelowRange = 0,
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            getMonitoringDetailsUseCase["calculateTir"](any<List<Glucose>>())
        }
        
        val expectedGlucoseRecommendation =
            Recommendations.HyperglycemiaRecommendation::class.java
        val actualGlucoseRecommendation =
            details.recommendations.glucoseRecommendation
        assertThat(actualGlucoseRecommendation)
            .isInstanceOf(expectedGlucoseRecommendation)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get details, given TIR below target and TBR dominates, return hypoglycemia recommendation as the glucose recommendation`() {
        every { getMonitoringDetailsUseCase["calculateTir"](any<List<Glucose>>()) } returns Tir(
            timeInRange = 0,
            timeAboveRange = 0,
            timeBelowRange = 100,
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            getMonitoringDetailsUseCase["calculateTir"](any<List<Glucose>>())
        }
        
        val expectedGlucoseRecommendation =
            Recommendations.HypoglycemiaRecommendation::class.java
        val actualGlucoseRecommendation =
            details.recommendations.glucoseRecommendation
        assertThat(actualGlucoseRecommendation)
            .isInstanceOf(expectedGlucoseRecommendation)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get details, given normal blood pressure, return normotension recommendation as blood pressure recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                null, null,
                systolic = 119, diastolic = 79,
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBloodPressureRecommendation =
            Recommendations.NormotensionRecommendation::class.java
        val actualBloodPressureRecommendation =
            details.recommendations.bloodPressureRecommendation
        assertThat(actualBloodPressureRecommendation)
            .isInstanceOf(expectedBloodPressureRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given elevated blood pressure, return normotension recommendation as blood pressure recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                null, null,
                systolic = 129, diastolic = 79,
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBloodPressureRecommendation =
            Recommendations.NormotensionRecommendation::class.java
        val actualBloodPressureRecommendation =
            details.recommendations.bloodPressureRecommendation
        assertThat(actualBloodPressureRecommendation)
            .isInstanceOf(expectedBloodPressureRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given hypertension stage 1 blood pressure, return hypertension recommendation as blood pressure recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                null, null,
                systolic = 139, diastolic = 89,
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBloodPressureRecommendation =
            Recommendations.HypertensionRecommendation::class.java
        val actualBloodPressureRecommendation =
            details.recommendations.bloodPressureRecommendation
        assertThat(actualBloodPressureRecommendation)
            .isInstanceOf(expectedBloodPressureRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given hypertension stage 2 blood pressure, return hypertension recommendation as blood pressure recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                null, null,
                systolic = 180, diastolic = 120,
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBloodPressureRecommendation =
            Recommendations.HypertensionRecommendation::class.java
        val actualBloodPressureRecommendation =
            details.recommendations.bloodPressureRecommendation
        assertThat(actualBloodPressureRecommendation)
            .isInstanceOf(expectedBloodPressureRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given hypertensive crisis blood pressure, return hypertensive crisis recommendation as blood pressure recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                null, null,
                systolic = 181, diastolic = 121,
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBloodPressureRecommendation =
            Recommendations.HypertensiveCrisisRecommendation::class.java
        val actualBloodPressureRecommendation =
            details.recommendations.bloodPressureRecommendation
        assertThat(actualBloodPressureRecommendation)
            .isInstanceOf(expectedBloodPressureRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given underweight BMI, return underweight recommendation as BMI recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                weight = 40, height = 170,
                null, null
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBmiRecommendation =
            Recommendations.UnderweightRecommendation::class.java
        val actualBmiRecommendation =
            details.recommendations.bmiRecommendation
        assertThat(actualBmiRecommendation)
            .isInstanceOf(expectedBmiRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given normal weight BMI, return normal weight recommendation as BMI recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                weight = 60, height = 170,
                null, null
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBmiRecommendation =
            Recommendations.NormalWeightRecommendation::class.java
        val actualBmiRecommendation =
            details.recommendations.bmiRecommendation
        assertThat(actualBmiRecommendation)
            .isInstanceOf(expectedBmiRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given overweight weight BMI, return overweight recommendation as BMI recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                weight = 80, height = 170,
                null, null
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBmiRecommendation =
            Recommendations.OverweightRecommendation::class.java
        val actualBmiRecommendation =
            details.recommendations.bmiRecommendation
        assertThat(actualBmiRecommendation)
            .isInstanceOf(expectedBmiRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given obesity class 1 BMI, return overweight recommendation as BMI recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                weight = 100, height = 170,
                null, null
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBmiRecommendation =
            Recommendations.OverweightRecommendation::class.java
        val actualBmiRecommendation =
            details.recommendations.bmiRecommendation
        assertThat(actualBmiRecommendation)
            .isInstanceOf(expectedBmiRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given obesity class 2 BMI, return overweight recommendation as BMI recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                weight = 110, height = 170,
                null, null
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBmiRecommendation =
            Recommendations.OverweightRecommendation::class.java
        val actualBmiRecommendation =
            details.recommendations.bmiRecommendation
        assertThat(actualBmiRecommendation)
            .isInstanceOf(expectedBmiRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get details, given obesity class 3 BMI, return overweight recommendation as BMI recommendation`() {
        every { fakePersonalDataRepository.getHealthNumbers() } returns Flowable.just(
            HealthNumbers(
                weight = 120, height = 170,
                null, null
            )
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedBmiRecommendation =
            Recommendations.OverweightRecommendation::class.java
        val actualBmiRecommendation =
            details.recommendations.bmiRecommendation
        assertThat(actualBmiRecommendation)
            .isInstanceOf(expectedBmiRecommendation)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @Test
    fun `Get event details, related glucose data of all events are not empty`() {
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getMonitoringDetailsUseCase(
            selectedPeriodFilter = MonitoringDetailsPeriodFilter.LAST_24_HOUR,
            customPeriod = null,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeEventRepository.getEventData()
            fakeGlucoseRepository.getGlucoseData()
        }
        
        assertThat(details.eventData.all { it.relatedGlucoseData.isNotEmpty() })
            .isTrue()
        
        confirmVerified(
            fakeEventRepository,
            fakeGlucoseRepository,
        )
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}

/*
@Test
    fun `Get details, given a null start date and end date, return details all the time`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        
        val dummyStartDate = null
        val dummyEndDate = null
        
        val observable = getMonitoringDetailsUseCase(
            startDate = dummyStartDate,
            endDate = dummyEndDate,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedGlucoseData = DummyData.ALL_GLUCOSES_SIZE
        val actualGlucoseData = details.glucoseData.size
        assertThat(actualGlucoseData)
            .isEqualTo(expectedGlucoseData)
        
        val expectedEventData = DummyData.ALL_EVENTS_SIZE
        val actualEventData = details.eventData.size
        assertThat(actualEventData)
            .isEqualTo(expectedEventData)
        
        val expectedGlucosesStats = GlucoseStats(
            lastValue = DummyData.ALL_GLUCOSES_LAST_VALUE,
            average = DummyData.ALL_GLUCOSES_AVERAGE,
            max = DummyData.ALL_GLUCOSES_MAX,
            min = DummyData.ALL_GLUCOSES_MIN,
        )
        val actualGlucosesStats = details.glucoseStats
        assertThat(actualGlucosesStats)
            .isEqualTo(expectedGlucosesStats)
        
        val expectedTir = Tir(
            timeInRange = DummyData.ALL_GLUCOSES_TIR,
            timeAboveRange = DummyData.ALL_GLUCOSES_TAR,
            timeBelowRange = DummyData.ALL_GLUCOSES_TBR,
        )
        val actualTir = details.tir
        assertThat(actualTir)
            .isEqualTo(expectedTir)
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @Test
    fun `Get details, given only a start date, return details on the start date`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        
        val startDate = DUMMY_DATE_1
        val endDate = null
        
        val observable = getMonitoringDetailsUseCase(
            startDate = startDate,
            endDate = endDate,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedDate = DUMMY_DATE_1
        assertThat(details.glucoseData.all { it.date == expectedDate })
            .isTrue()
        assertThat(details.eventData.all { it.date == expectedDate })
            .isTrue()
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @Test
    fun `Get details, given both start date and end date, return details from start to end date`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        
        val startDate = DUMMY_DATE_1
        val endDate = DUMMY_DATE_2
        
        val observable = getMonitoringDetailsUseCase(
            startDate = startDate,
            endDate = endDate,
        )
        val testObservable = observable.test()
        val details = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedDate = DUMMY_DATE_1..DUMMY_DATE_2
        assertThat(details.glucoseData.all { it.date in expectedDate })
            .isTrue()
        assertThat(details.eventData.all { it.date in expectedDate })
            .isTrue()
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
*/