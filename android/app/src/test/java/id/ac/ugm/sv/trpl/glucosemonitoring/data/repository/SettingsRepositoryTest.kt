package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.SettingsMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.SettingsLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsRepositoryTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsLocalDataSource: SettingsLocalDataSource
    private lateinit var settingsMapper: SettingsMapper
    
    @Before
    fun setUp() {
        settingsLocalDataSource = mockk()
        settingsMapper = mockk()
        
        settingsRepository = SettingsRepository(
            settingsLocalDataSource = settingsLocalDataSource,
            settingsMapper = settingsMapper,
        )
    }
    
    @Suppress("UnnecessaryVariable")
    @Test
    fun `Get settings, settings`() {
        val dummyEnableGlucoseAlarms = true
        val dummyMappedSettings = Settings(
            enableGlucoseAlarms = true,
        )
        
        every { settingsLocalDataSource.getEnableGlucoseAlarms() } returns Flowable.just(
            dummyEnableGlucoseAlarms
        )
        every {
            settingsMapper.mapToDomain(
                enableGlucoseAlarms = eq(dummyEnableGlucoseAlarms),
            )
        } returns
                dummyMappedSettings
        
        val observable = settingsRepository.getSettings()
        val testObservable = observable.test()
        
        verifySequence {
            settingsLocalDataSource.getEnableGlucoseAlarms()
            settingsMapper.mapToDomain(
                enableGlucoseAlarms = eq(dummyEnableGlucoseAlarms),
            )
        }
        
        val expectedResult = dummyMappedSettings
        testObservable.assertResult(expectedResult)
        
        confirmVerified(
            settingsLocalDataSource,
            settingsMapper,
        )
    }
    
    @Test
    fun `Save settings, return complete`() {
        val dummyEnableGlucoseAlarms = true
        
        every { settingsLocalDataSource.saveEnableGlucoseAlarms(eq(dummyEnableGlucoseAlarms)) } returns
                Completable.complete()
        
        val observable = settingsRepository.saveSettings(dummyEnableGlucoseAlarms)
        val testObservable = observable.test()
        
        verify {
            settingsLocalDataSource.saveEnableGlucoseAlarms(eq(dummyEnableGlucoseAlarms))
        }
        
        testObservable.assertComplete()
        
        confirmVerified(settingsLocalDataSource)
    }
    
    @Test
    fun `Clear settings data, return complete`() {
        every { settingsLocalDataSource.clearData() } returns
                Completable.complete()
        
        val observable = settingsRepository.resetSettings()
        val testObservable = observable.test()
        
        verify {
            settingsLocalDataSource.clearData()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(settingsLocalDataSource)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}