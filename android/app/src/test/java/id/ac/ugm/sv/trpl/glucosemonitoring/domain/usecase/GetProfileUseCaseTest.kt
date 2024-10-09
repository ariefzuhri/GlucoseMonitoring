package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.User
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetProfileUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getProfileUseCase: GetProfileUseCase
    private lateinit var fakeUserRepository: FakeUserRepository
    
    @Before
    fun setUp() {
        fakeUserRepository = spyk(FakeUserRepository())
        
        getProfileUseCase = GetProfileUseCase(
            userRepository = fakeUserRepository,
        )
    }
    
    @Test
    fun `Get profile, return profile`() {
        val observable = getProfileUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeUserRepository.getProfile()
        }
        
        val expectedResult = User(-1, "", "")
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeUserRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}