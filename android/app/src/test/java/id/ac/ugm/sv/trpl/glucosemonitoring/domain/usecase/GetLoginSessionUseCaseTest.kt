package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLoginSessionUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getLoginSessionUseCase: GetLoginSessionUseCase
    private lateinit var fakeUserRepository: FakeUserRepository
    
    @Before
    fun setUp() {
        fakeUserRepository = spyk(FakeUserRepository())
        
        getLoginSessionUseCase = GetLoginSessionUseCase(
            userRepository = fakeUserRepository,
        )
    }
    
    @Test
    fun `Get login session, return is logged in`() {
        val observable = getLoginSessionUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeUserRepository.getProfile()
        }
        
        val expectedResult = false
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeUserRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}