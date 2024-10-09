package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import com.google.common.truth.Truth.assertThat
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetEventListUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getEventListUseCase: GetEventListUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        
        getEventListUseCase = GetEventListUseCase(
            eventRepository = fakeEventRepository,
            glucoseRepository = fakeGlucoseRepository,
        )
        
        every { fakeEventRepository.getEventData() } returns Flowable.just(
            DummyData.allEvents
        )
        every { fakeGlucoseRepository.getGlucoseData() } returns Flowable.just(
            DummyData.allGlucoses
        )
    }
    
    @Test
    fun `Get event list, return all events`() {
        val observable = getEventListUseCase()
        val testObservable = observable.test()
        
        verifySequence {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        val expectedEventList = DummyData.allEvents
        testObservable.assertResult(expectedEventList)
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @Test
    fun `Get event list, only the meal-type events contain glucose related data`() {
        val observable = getEventListUseCase()
        val testObservable = observable.test()
        val eventList = testObservable.values().first()
        
        verifySequence {
            fakeGlucoseRepository.getGlucoseData()
            fakeEventRepository.getEventData()
        }
        
        assertThat(eventList.all { event ->
            if (event.type == EventType.MEAL) {
                event.relatedGlucoseData.isNotEmpty()
            } else {
                event.relatedGlucoseData.isEmpty()
            }
        }).isTrue()
        
        confirmVerified(
            fakeGlucoseRepository,
            fakeEventRepository,
        )
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}