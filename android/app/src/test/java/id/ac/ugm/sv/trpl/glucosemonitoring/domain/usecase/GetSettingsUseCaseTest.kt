package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeSettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetSettingsUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var getSettingsUseCase: GetSettingsUseCase
    private lateinit var fakeSettingsRepository: FakeSettingsRepository
    
    @Before
    fun setUp() {
        fakeSettingsRepository = spyk(FakeSettingsRepository())
        
        getSettingsUseCase = GetSettingsUseCase(
            settingsRepository = fakeSettingsRepository,
        )
    }
    
    @Test
    fun `Get settings, return settings`() {
        val observable = getSettingsUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeSettingsRepository.getSettings()
        }
        
        val expectedResult = Settings(false)
        testObservable.assertResult(expectedResult)
        
        confirmVerified(fakeSettingsRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}