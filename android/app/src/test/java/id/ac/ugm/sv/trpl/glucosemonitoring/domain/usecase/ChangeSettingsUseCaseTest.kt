package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeSettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChangeSettingsUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var changeSettingsUseCase: ChangeSettingsUseCase
    private lateinit var fakeSettingsRepository: FakeSettingsRepository
    
    @Before
    fun setUp() {
        fakeSettingsRepository = spyk(FakeSettingsRepository())
        
        changeSettingsUseCase = ChangeSettingsUseCase(
            settingsRepository = fakeSettingsRepository,
        )
    }
    
    @Test
    fun `Change enable glucose alarms, return complete`() {
        val dummyEnableGlucoseAlarms = true
        
        val observable = changeSettingsUseCase(
            enableGlucoseAlarms = dummyEnableGlucoseAlarms,
        )
        val testObservable = observable.test()
        
        verify {
            fakeSettingsRepository.saveSettings(
                enableGlucoseAlarms = eq(dummyEnableGlucoseAlarms),
            )
        }
        
        testObservable.assertComplete()
        
        confirmVerified(fakeSettingsRepository)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}