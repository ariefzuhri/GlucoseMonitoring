package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditEventUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var editEventUseCase: EditEventUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    
    private val dummyId = 1
    private val dummyType = EventType.MEAL
    private val dummyDesc = "Lorem ipsum"
    private val dummyDate = "2000-01-01"
    private val dummyTime = "07:00"
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        
        editEventUseCase = EditEventUseCase(
            eventRepository = fakeEventRepository,
            userRepository = FakeUserRepository(),
        )
        
        fakeEventRepository.addEvent(
            patientId = dummyId,
            type = dummyType.id,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
    }
    
    @Test
    fun `Edit an event, return success`() {
        val observable = editEventUseCase(
            id = dummyId,
            type = dummyType,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
        val testObservable = observable.test()
        
        verify {
            fakeEventRepository.editEvent(
                patientId = any(),
                id = eq(dummyId),
                type = eq(dummyType.id),
                desc = eq(dummyDesc),
                date = eq(dummyDate),
                time = eq(dummyTime),
            )
        }
        
        val expectedResult = Result.Success<Nothing>()
        testObservable.assertResult(expectedResult)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}