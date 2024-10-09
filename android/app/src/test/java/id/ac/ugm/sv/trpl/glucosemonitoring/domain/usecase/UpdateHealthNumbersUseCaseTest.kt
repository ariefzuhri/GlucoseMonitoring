package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UpdateHealthNumbersUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var updateHealthNumbersUseCase: UpdateHealthNumbersUseCase
    private lateinit var fakePersonalDataRepository: FakeHealthNumbersRepository
    
    @Before
    fun setUp() {
        fakePersonalDataRepository = spyk(FakeHealthNumbersRepository())
        
        updateHealthNumbersUseCase = UpdateHealthNumbersUseCase(
            healthNumbersRepository = fakePersonalDataRepository,
        )
    }
    
    @Test
    fun `Edit personal data, return complete`() {
        val observable = updateHealthNumbersUseCase(
            null, null, null, null,
        )
        val testObservable = observable.test()
        
        verify {
            fakePersonalDataRepository.saveHealthNumbers(
                any(), any(), any(), any(),
            )
        }
        
        testObservable.assertComplete()
        
        confirmVerified(fakePersonalDataRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}