package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verifySequence
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var fakeUserRepository: FakeUserRepository
    
    @Before
    fun setUp() {
        fakeUserRepository = spyk(FakeUserRepository())
        
        loginUseCase = LoginUseCase(
            userRepository = fakeUserRepository,
        )
    }
    
    @Test
    fun `Login, return success`() {
        val dummyEmail = "johndoe@mail.com"
        val dummyPassword = "123456"
        
        val observable = loginUseCase(
            email = dummyEmail,
            password = dummyPassword,
        )
        val testObservable = observable.test()
        
        verifySequence {
            fakeUserRepository.login(
                email = eq(dummyEmail),
                password = eq(dummyPassword),
            )
            fakeUserRepository.saveProfile(
                patientId = any(),
                name = any(),
                email = eq(dummyEmail),
            )
        }
        
        val expectedResult = Result.Success(null)
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeUserRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}