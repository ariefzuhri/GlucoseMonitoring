package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import com.google.common.truth.Truth.assertThat
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import id.ac.ugm.sv.trpl.glucosemonitoring.util.Constants
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.properties.Delegates

class SelectEventUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var selectEventUseCase: SelectEventUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    
    private var dummyId by Delegates.notNull<Int>()
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        
        selectEventUseCase = SelectEventUseCase(
            eventRepository = fakeEventRepository,
        )
        
        fakeEventRepository.addEvent(
            patientId = Constants.DummyPatientID.DEFAULT,
            type = EventType.MEAL.id,
            desc = "Lorem ipsum",
            date = "2000-01-01",
            time = "07:00",
        )
        dummyId = fakeEventRepository.getEventData().test().values().first().first().id
    }
    
    @Test
    fun `Select an event, return the event selected`() {
        val observable = selectEventUseCase(dummyId)
        val testObservable = observable.test()
        val event = testObservable.values().first()
        
        verify {
            fakeEventRepository.getEventData()
        }
        
        val expectedEvent = dummyId
        val actualEvent = event.id
        assertThat(actualEvent)
            .isEqualTo(expectedEvent)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}