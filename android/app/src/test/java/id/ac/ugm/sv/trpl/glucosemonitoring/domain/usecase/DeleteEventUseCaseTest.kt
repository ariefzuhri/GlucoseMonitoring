package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteEventUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var deleteEventUseCase: DeleteEventUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    
    private val dummyId = 1
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        
        deleteEventUseCase = DeleteEventUseCase(
            eventRepository = fakeEventRepository,
        )
        
        fakeEventRepository.addEvent(
            patientId = dummyId,
            type = EventType.MEAL.id,
            desc = "Lorem ipsum",
            date = "2000-01-01",
            time = "07:00",
        )
    }
    
    @Test
    fun `Delete an event, return complete`() {
        val observable = deleteEventUseCase(dummyId)
        val testObservable = observable.test()
        
        verify {
            fakeEventRepository.deleteEvent(eq(dummyId))
        }
        
        testObservable.assertComplete()
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}