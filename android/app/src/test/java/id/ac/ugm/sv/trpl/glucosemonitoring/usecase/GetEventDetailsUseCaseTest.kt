package id.ac.ugm.sv.trpl.glucosemonitoring.usecase

import com.google.common.truth.Truth.assertThat
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.GetEventDetailsUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetEventDetailsUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getEventDetailsUseCase: GetEventDetailsUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    
    private val dummyMealId = DummyData.event1.id
    private val dummyExerciseId = DummyData.event2.id
    private val dummyMedicationId = DummyData.event3.id
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        
        getEventDetailsUseCase = GetEventDetailsUseCase(
            eventRepository = fakeEventRepository,
            glucoseRepository = fakeGlucoseRepository,
        )
        
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
    }
    
    @Test
    fun `Get event details, given the id of an event, return correct event`() {
        val observable = getEventDetailsUseCase(
            id = dummyMealId,
        )
        val testObservable = observable.test()
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedEvent = DummyData.event1
        val actualEvent = testObservable.values().first().first
        assertThat(actualEvent)
            .isEqualTo(expectedEvent)
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get event details, given the id of a meal event, correct related event data`() {
        val observable = getEventDetailsUseCase(
            id = dummyMealId,
        )
        val testObservable = observable.test()
        val eventRelatedData = testObservable.values().first().second
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedRelatedEventData = DummyData.EVENT_1_RELATED_EVENT_DATA
        val actualRelatedEventData = eventRelatedData.map { it.id }
        assertThat(actualRelatedEventData)
            .containsExactlyElementsIn(expectedRelatedEventData)
            .inOrder()
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get event details, given the id of an exercise event, correct related event data`() {
        val observable = getEventDetailsUseCase(
            id = dummyExerciseId,
        )
        val testObservable = observable.test()
        val eventRelatedData = testObservable.values().first().second
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedRelatedEventData = DummyData.EVENT_2_RELATED_EVENT_DATA
        val actualRelatedEventData = eventRelatedData.map { it.id }
        assertThat(actualRelatedEventData)
            .containsExactlyElementsIn(expectedRelatedEventData)
            .inOrder()
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get event details, given the id of a medication event, correct related event data`() {
        val observable = getEventDetailsUseCase(
            id = dummyMedicationId,
        )
        val testObservable = observable.test()
        val eventRelatedData = testObservable.values().first().second
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedRelatedEventData = DummyData.EVENT_3_RELATED_EVENT_DATA
        val actualRelatedEventData = eventRelatedData.map { it.id }
        assertThat(actualRelatedEventData)
            .containsExactlyElementsIn(expectedRelatedEventData)
            .inOrder()
        
        confirmVerified(fakeEventRepository)
    }
    
    @Test
    fun `Get event details, given the id of a meal event, correct related glucose data`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getEventDetailsUseCase(
            id = dummyMealId,
        )
        val testObservable = observable.test()
        val event = testObservable.values().first().first
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedRelatedGlucoseData = DummyData.EVENT_1_RELATED_GLUCOSE_DATA
        val actualRelatedGlucoseData = event.relatedGlucoseData.map { it.id }
        assertThat(actualRelatedGlucoseData)
            .containsExactlyElementsIn(expectedRelatedGlucoseData)
            .inOrder()
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @Test
    fun `Get event details, given the id of an exercise event, correct related glucose data`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getEventDetailsUseCase(
            id = dummyExerciseId,
        )
        val testObservable = observable.test()
        val event = testObservable.values().first().first
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedRelatedGlucoseData = DummyData.EVENT_2_RELATED_GLUCOSE_DATA
        val actualRelatedGlucoseData = event.relatedGlucoseData.map { it.id }
        assertThat(actualRelatedGlucoseData)
            .containsExactlyElementsIn(expectedRelatedGlucoseData)
            .inOrder()
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @Test
    fun `Get event details, given the id of a medication event, correct related glucose data`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getEventDetailsUseCase(
            id = dummyMedicationId,
        )
        val testObservable = observable.test()
        val event = testObservable.values().first().first
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedRelatedGlucoseData = DummyData.EVENT_3_RELATED_GLUCOSE_DATA
        val actualRelatedGlucoseData = event.relatedGlucoseData.map { it.id }
        assertThat(actualRelatedGlucoseData)
            .containsExactlyElementsIn(expectedRelatedGlucoseData)
            .inOrder()
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @Test
    fun `Get event details, given the id of a meal event, correct initial, final, and glucose changes`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getEventDetailsUseCase(
            id = dummyMealId,
        )
        val testObservable = observable.test()
        val event = testObservable.values().first().first
        
        val expectedInitialGlucose = DummyData.EVENT_1_INITIAL_GLUCOSE
        val actualInitialGlucose = event.initialGlucose
        assertThat(actualInitialGlucose)
            .isEqualTo(expectedInitialGlucose)
        
        val expectedFinalGlucose = DummyData.EVENT_1_FINAL_GLUCOSE
        val actualFinalGlucose = event.finalGlucose
        assertThat(actualFinalGlucose)
            .isEqualTo(expectedFinalGlucose)
        
        val expectedGlucoseChanges = DummyData.EVENT_1_GLUCOSE_CHANGES
        val actualGlucoseChanges = event.glucoseChanges
        assertThat(actualGlucoseChanges)
            .isEqualTo(expectedGlucoseChanges)
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
    }
    
    @Test
    fun `Get event details, given the id of an exercise event, correct initial, final, and glucose changes`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getEventDetailsUseCase(
            id = dummyExerciseId,
        )
        val testObservable = observable.test()
        val event = testObservable.values().first().first
        
        val expectedInitialGlucose = DummyData.EVENT_2_INITIAL_GLUCOSE
        val actualInitialGlucose = event.initialGlucose
        assertThat(actualInitialGlucose)
            .isEqualTo(expectedInitialGlucose)
        
        val expectedFinalGlucose = DummyData.EVENT_2_FINAL_GLUCOSE
        val actualFinalGlucose = event.finalGlucose
        assertThat(actualFinalGlucose)
            .isEqualTo(expectedFinalGlucose)
        
        val expectedGlucoseChanges = DummyData.EVENT_2_GLUCOSE_CHANGES
        val actualGlucoseChanges = event.glucoseChanges
        assertThat(actualGlucoseChanges)
            .isEqualTo(expectedGlucoseChanges)
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
    }
    
    @Test
    fun `Get event details, given the id of a medication event, correct initial, final, and glucose changes`() {
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
        
        val observable = getEventDetailsUseCase(
            id = dummyMedicationId,
        )
        val testObservable = observable.test()
        val event = testObservable.values().first().first
        
        val expectedInitialGlucose = DummyData.EVENT_3_INITIAL_GLUCOSE
        val actualInitialGlucose = event.initialGlucose
        assertThat(actualInitialGlucose)
            .isEqualTo(expectedInitialGlucose)
        
        val expectedFinalGlucose = DummyData.EVENT_3_FINAL_GLUCOSE
        val actualFinalGlucose = event.finalGlucose
        assertThat(actualFinalGlucose)
            .isEqualTo(expectedFinalGlucose)
        
        val expectedGlucoseChanges = DummyData.EVENT_3_GLUCOSE_CHANGES
        val actualGlucoseChanges = event.glucoseChanges
        assertThat(actualGlucoseChanges)
            .isEqualTo(expectedGlucoseChanges)
        
        verify {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}