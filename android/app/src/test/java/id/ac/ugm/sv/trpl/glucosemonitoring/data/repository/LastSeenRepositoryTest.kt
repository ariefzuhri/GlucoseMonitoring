package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.LastSeenMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.LastSeenLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.LastSeen
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

class LastSeenRepositoryTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var lastSeenRepository: LastSeenRepository
    private lateinit var lastSeenLocalDataSource: LastSeenLocalDataSource
    private lateinit var lastSeenMapper: LastSeenMapper
    
    @Before
    fun setUp() {
        lastSeenLocalDataSource = mockk()
        lastSeenMapper = mockk()
        
        lastSeenRepository = LastSeenRepository(
            lastSeenLocalDataSource = lastSeenLocalDataSource,
            lastSeenMapper = lastSeenMapper,
        )
    }
    
    @Suppress("UnnecessaryVariable")
    @Test
    fun `Get last seen, return last seen`() {
        val dummyGlucoseLevel = "100"
        val dummyGlucoseTime = "2000-01-01 00:00"
        val dummyMappedLastSeen = LastSeen(
            glucoseLevel = 100,
            glucoseTime = "2000-01-01 00:00",
        )
        
        every { lastSeenLocalDataSource.getGlucoseLevel() } returns Flowable.just(
            dummyGlucoseLevel
        )
        every { lastSeenLocalDataSource.getGlucoseTime() } returns Flowable.just(
            dummyGlucoseTime
        )
        every {
            lastSeenMapper.mapToDomain(
                glucoseLevel = eq(dummyGlucoseLevel),
                glucoseTime = eq(dummyGlucoseTime),
            )
        } returns
                dummyMappedLastSeen
        
        val observable = lastSeenRepository.getLastSeen()
        val testObservable = observable.test()
        
        verifySequence {
            lastSeenLocalDataSource.getGlucoseLevel()
            lastSeenLocalDataSource.getGlucoseTime()
            lastSeenMapper.mapToDomain(
                glucoseLevel = eq(dummyGlucoseLevel),
                glucoseTime = eq(dummyGlucoseTime),
            )
        }
        
        val expectedResult = dummyMappedLastSeen
        testObservable.assertResult(expectedResult)
        
        confirmVerified(
            lastSeenLocalDataSource,
            lastSeenMapper,
        )
    }
    
    @Test
    fun `Save last seen, return complete`() {
        val dummyGlucoseLevel = 100
        val dummyGlucoseTime = "2000-01-01 00:00"
        
        every { lastSeenLocalDataSource.saveGlucoseLevel(any()) } returns
                Completable.complete()
        every { lastSeenLocalDataSource.saveGlucoseTime(any()) } returns
                Completable.complete()
        
        val observable = lastSeenRepository.saveLastSeen(
            glucoseLevel = dummyGlucoseLevel,
            glucoseTime = dummyGlucoseTime,
        )
        val testObservable = observable.test()
        
        verify {
            lastSeenLocalDataSource.saveGlucoseLevel(eq(dummyGlucoseLevel.toString()))
            lastSeenLocalDataSource.saveGlucoseTime(eq(dummyGlucoseTime))
        }
        
        testObservable.assertComplete()
        
        confirmVerified(lastSeenLocalDataSource)
    }
    
    @Test
    fun `Clear last seen data, return complete`() {
        every { lastSeenLocalDataSource.clearData() } returns
                Completable.complete()
        
        val observable = lastSeenRepository.clearLastSeenData()
        val testObservable = observable.test()
        
        verify {
            lastSeenLocalDataSource.clearData()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(lastSeenLocalDataSource)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}