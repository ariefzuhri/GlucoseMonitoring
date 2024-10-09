package id.ac.ugm.sv.trpl.glucosemonitoring.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase.AddEventUseCase
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AddEventUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var addEventUseCase: AddEventUseCase
    private lateinit var fakeEventRepository: FakeEventRepository
    
    @Before
    fun setUp() {
        fakeEventRepository = spyk(FakeEventRepository())
        
        addEventUseCase = AddEventUseCase(
            eventRepository = fakeEventRepository,
            userRepository = FakeUserRepository(),
        )
    }
    
    @Test
    fun `Add an event, return success`() {
        val dummyType = EventType.MEAL
        val dummyDesc = "Lorem ipsum"
        val dummyDate = "2000-01-01"
        val dummyTime = "07:00"
        
        val observable = addEventUseCase(
            type = dummyType,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
        val testObservable = observable.test()
        
        verify {
            fakeEventRepository.addEvent(
                patientId = any(),
                type = eq(dummyType.id),
                desc = eq(dummyDesc),
                date = eq(dummyDate),
                time = eq(dummyTime),
            )
        }
        
        val expectedResult = Result.Success<Nothing>()
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeEventRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}