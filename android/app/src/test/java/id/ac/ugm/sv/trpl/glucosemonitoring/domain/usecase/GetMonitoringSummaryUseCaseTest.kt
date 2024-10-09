package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import com.google.common.truth.Truth.assertThat
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeLastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.LastSeen
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringSummary
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyAll
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMonitoringSummaryUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getMonitoringSummaryUseCase: GetMonitoringSummaryUseCase
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    private lateinit var fakeEventRepository: FakeEventRepository
    private lateinit var fakeLastSeenRepository: FakeLastSeenRepository
    
    @Before
    fun setUp() {
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        fakeEventRepository = spyk(FakeEventRepository())
        fakeLastSeenRepository = spyk(FakeLastSeenRepository())
        
        getMonitoringSummaryUseCase = GetMonitoringSummaryUseCase(
            glucoseRepository = fakeGlucoseRepository,
            eventRepository = fakeEventRepository,
            lastSeenRepository = fakeLastSeenRepository,
        )
        
        /* val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dummyCurrentDate = formatter.parse(Constants.DUMMY_DATE_2) as Date
        
        mockkStatic(::getCurrentDateTime)
        every { getCurrentDateTime() } returns dummyCurrentDate */
    }
    
    @Test
    fun `Get overview, correct latest glucose`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getMonitoringSummaryUseCase()
        val testObservable = observable.test()
        val overview = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
        }
        
        val expectedLatestGlucoseLevel = DummyData.ALL_GLUCOSES_LAST_VALUE
        val actualLatestGlucoseLevel = overview.latestGlucoseLevel
        assertThat(actualLatestGlucoseLevel)
            .isEqualTo(expectedLatestGlucoseLevel)
        
        val expectedLatestGlucoseCategory = DummyData.ALL_GLUCOSES_LAST_CATEGORY
        val actualLatestGlucoseCategory = overview.latestGlucoseCategory
        assertThat(actualLatestGlucoseCategory)
            .isEqualTo(expectedLatestGlucoseCategory)
        
        val expectedLatestGlucoseTime = DummyData.ALL_GLUCOSES_LAST_TIME
        val actualLatestGlucoseTime = overview.latestGlucoseTime
        assertThat(actualLatestGlucoseTime)
            .isEqualTo(expectedLatestGlucoseTime)
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Test
    fun `Get overview, correct today glucose data`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getMonitoringSummaryUseCase()
        val testObservable = observable.test()
        val overview = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
        }
        
        val expectedTodayGlucoseData = DummyData.dummyDate2Glucoses
        val actualTodayGlucoseData = overview.latestGlucoseData
        assertThat(actualTodayGlucoseData)
            .containsExactlyElementsIn(expectedTodayGlucoseData)
            .inOrder()
        
        confirmVerified(fakeGlucoseRepository)
    }
    
    @Suppress("UnnecessaryVariable")
    @Test
    fun `Get overview, correct last seen`() {
        val dummyLastSeenGlucoseLevel = 60
        val dummyLastSeenGlucoseTime = "2021-01-02 11:30"
        
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        every { fakeLastSeenRepository.getLastSeen() } returns Flowable.just(
            LastSeen(
                glucoseLevel = dummyLastSeenGlucoseLevel,
                glucoseTime = dummyLastSeenGlucoseTime,
            )
        )
        
        val observable = getMonitoringSummaryUseCase()
        val testObservable = observable.test()
        val overview = testObservable.values().first()
        
        verifyAll {
            fakeGlucoseRepository.getGlucoseData()
            fakeLastSeenRepository.getLastSeen()
        }
        
        val expectedLastSeenGlucoseChanges = -10
        val actualLastSeenGlucoseChanges = overview.lastSeenGlucoseChanges
        assertThat(actualLastSeenGlucoseChanges)
            .isEqualTo(expectedLastSeenGlucoseChanges)
        
        val expectedLastSeenGlucoseTime = dummyLastSeenGlucoseTime
        val actualLastSeenGlucoseTime = overview.lastSeenGlucoseTime
        assertThat(actualLastSeenGlucoseTime)
            .isEqualTo(expectedLastSeenGlucoseTime)
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeLastSeenRepository,
        )
    }
    
    @Test
    fun `Get overview, correct last events from all types`() {
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        
        val observable = getMonitoringSummaryUseCase()
        val testObservable = observable.test()
        val overview = testObservable.values().first()
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedLastMeal = DummyData.ALL_EVENTS_LAST_MEAL
        val actualLastMeal = overview.lastMeal?.id
        assertThat(actualLastMeal)
            .isEqualTo(expectedLastMeal)
        
        val expectedLastExercise = DummyData.ALL_EVENTS_LAST_EXERCISE
        val actualLastExercise = overview.lastExercise?.id
        assertThat(actualLastExercise)
            .isEqualTo(expectedLastExercise)
        
        val expectedLastMedication = DummyData.ALL_EVENTS_LAST_MEDICATION
        val actualLastMedication = overview.lastMedication?.id
        assertThat(actualLastMedication)
            .isEqualTo(expectedLastMedication)
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get overview, only the meal-type events contain glucose related data`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        
        val observable = getMonitoringSummaryUseCase()
        val testObservable = observable.test()
        val overview = testObservable.values().first()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedLastMealRelatedGlucoseData = DummyData.EVENT_4_RELATED_GLUCOSE_DATA
        val actualLastMealRelatedGlucoseData = overview.lastMeal
            ?.relatedGlucoseData?.map { it.id }
        assertThat(actualLastMealRelatedGlucoseData)
            .containsExactlyElementsIn(expectedLastMealRelatedGlucoseData)
            .inOrder()
        
        // This both related glucose data are not necessary for monitoringSummary
        val expectedLastExerciseRelatedGlucoseData = listOf<Int>()
        val actualLastExerciseRelatedGlucoseData = overview.lastExercise
            ?.relatedGlucoseData?.map { it.id }
        assertThat(actualLastExerciseRelatedGlucoseData)
            .containsExactlyElementsIn(expectedLastExerciseRelatedGlucoseData)
        
        val expectedLastMedicationRelatedGlucoseData = listOf<Int>()
        val actualLastMedicationRelatedGlucoseData = overview.lastMedication
            ?.relatedGlucoseData?.map { it.id }
        assertThat(actualLastMedicationRelatedGlucoseData)
            .containsExactlyElementsIn(expectedLastMedicationRelatedGlucoseData)
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get overview, given all data (glucoses, events, and last seen) are empty, return empty overview`() {
        val observable = getMonitoringSummaryUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
            fakeLastSeenRepository.getLastSeen()
        }
        
        val expectedMonitoringSummary = MonitoringSummary(
            latestGlucoseCategory = GlucoseCategory.UNKNOWN,
            latestGlucoseLevel = null,
            latestGlucoseTime = null,
            latestGlucoseData = emptyList(),
            lastSeenGlucoseChanges = null,
            lastSeenGlucoseTime = null,
            lastMeal = null,
            lastExercise = null,
            lastMedication = null,
        )
        testObservable.assertResult(expectedMonitoringSummary)
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
            fakeLastSeenRepository,
        )
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}