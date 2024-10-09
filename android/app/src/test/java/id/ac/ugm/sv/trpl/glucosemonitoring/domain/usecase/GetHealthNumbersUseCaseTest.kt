package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetHealthNumbersUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getHealthNumbersUseCase: GetHealthNumbersUseCase
    private lateinit var fakePersonalDataRepository: FakeHealthNumbersRepository
    
    @Before
    fun setUp() {
        fakePersonalDataRepository = spyk(FakeHealthNumbersRepository())
        
        getHealthNumbersUseCase = GetHealthNumbersUseCase(
            healthNumbersRepository = fakePersonalDataRepository,
        )
    }
    
    @Test
    fun `Get personal data, return personal data`() {
        val observable = getHealthNumbersUseCase()
        val testObservable = observable.test()
        
        verify {
            fakePersonalDataRepository.getHealthNumbers()
        }
        
        val expectedResult = HealthNumbers(null, null, null, null)
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}