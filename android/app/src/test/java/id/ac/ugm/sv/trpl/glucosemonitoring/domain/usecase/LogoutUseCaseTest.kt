package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeLastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeSettingsRepository
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

class LogoutUseCaseTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var fakeGlucoseRepository: FakeGlucoseRepository
    private lateinit var fakeEventRepository: FakeEventRepository
    private lateinit var fakePersonalDataRepository: FakeHealthNumbersRepository
    private lateinit var fakeLastSeenRepository: FakeLastSeenRepository
    private lateinit var fakeSettingsRepository: FakeSettingsRepository
    
    @Before
    fun setUp() {
        fakeUserRepository = spyk(FakeUserRepository())
        fakeGlucoseRepository = spyk(FakeGlucoseRepository())
        fakeEventRepository = spyk(FakeEventRepository())
        fakePersonalDataRepository = spyk(FakeHealthNumbersRepository())
        fakeLastSeenRepository = spyk(FakeLastSeenRepository())
        fakeSettingsRepository = spyk(FakeSettingsRepository())
        
        logoutUseCase = LogoutUseCase(
            userRepository = fakeUserRepository,
            glucoseRepository = fakeGlucoseRepository,
            eventRepository = fakeEventRepository,
            iHealthNumbersRepository = fakePersonalDataRepository,
            lastSeenRepository = fakeLastSeenRepository,
            settingsRepository = fakeSettingsRepository,
        )
    }
    
    @Test
    fun `Logout, return complete`() {
        val observable = logoutUseCase()
        val testObservable = observable.test()
        
        verify {
            fakeUserRepository.clearUserData()
            fakeGlucoseRepository.clearGlucoseData()
            fakeEventRepository.clearEventData()
            fakePersonalDataRepository.clearHealthNumbersData()
            fakeLastSeenRepository.clearLastSeenData()
            fakeSettingsRepository.resetSettings()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(
            fakeUserRepository,
            fakeGlucoseRepository,
            fakeEventRepository,
            fakePersonalDataRepository,
            fakeLastSeenRepository,
            fakeSettingsRepository,
        )
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}