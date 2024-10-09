package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.HealthNumbersMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.HealthNumbersLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
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

class HealthNumbersRepositoryTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var personalDataRepository: HealthNumbersRepository
    private lateinit var healthNumbersLocalDataSource: HealthNumbersLocalDataSource
    private lateinit var healthNumbersMapper: HealthNumbersMapper
    
    @Before
    fun setUp() {
        healthNumbersLocalDataSource = mockk()
        healthNumbersMapper = mockk()
        
        personalDataRepository = HealthNumbersRepository(
            healthNumbersLocalDataSource = healthNumbersLocalDataSource,
            healthNumbersMapper = healthNumbersMapper,
        )
    }
    
    @Suppress("UnnecessaryVariable")
    @Test
    fun `Get personal data, return personal data`() {
        val dummyWeight = "60"
        val dummyHeight = "170"
        val dummySystolic = "120"
        val dummyDiastolic = "80"
        val dummyMappedHealthNumbers = HealthNumbers(
            weight = 60,
            height = 170,
            systolic = 120,
            diastolic = 80,
        )
        
        every { healthNumbersLocalDataSource.getWeight() } returns Flowable.just(
            dummyWeight
        )
        every { healthNumbersLocalDataSource.getHeight() } returns Flowable.just(
            dummyHeight
        )
        every { healthNumbersLocalDataSource.getSystolic() } returns Flowable.just(
            dummySystolic
        )
        every { healthNumbersLocalDataSource.getDiastolic() } returns Flowable.just(
            dummyDiastolic
        )
        every {
            healthNumbersMapper.mapToDomain(
                weight = eq(dummyWeight),
                height = eq(dummyHeight),
                systolic = eq(dummySystolic),
                diastolic = eq(dummyDiastolic),
            )
        } returns
                dummyMappedHealthNumbers
        
        val observable = personalDataRepository.getHealthNumbers()
        val testObservable = observable.test()
        
        verifySequence {
            healthNumbersLocalDataSource.getWeight()
            healthNumbersLocalDataSource.getHeight()
            healthNumbersLocalDataSource.getSystolic()
            healthNumbersLocalDataSource.getDiastolic()
            healthNumbersMapper.mapToDomain(
                weight = eq(dummyWeight),
                height = eq(dummyHeight),
                systolic = eq(dummySystolic),
                diastolic = eq(dummyDiastolic),
            )
        }
        
        val expectedResult = dummyMappedHealthNumbers
        testObservable.assertResult(expectedResult)
        
        confirmVerified(
            healthNumbersLocalDataSource,
            healthNumbersMapper,
        )
    }
    
    @Test
    fun `Save personal data, return complete`() {
        val dummyWeight = 60
        val dummyHeight = 170
        val dummySystolic = 120
        val dummyDiastolic = 80
        
        every { healthNumbersLocalDataSource.saveWeight(any()) } returns
                Completable.complete()
        every { healthNumbersLocalDataSource.saveHeight(any()) } returns
                Completable.complete()
        every { healthNumbersLocalDataSource.saveSystolic(any()) } returns
                Completable.complete()
        every { healthNumbersLocalDataSource.saveDiastolic(any()) } returns
                Completable.complete()
        
        val observable = personalDataRepository.saveHealthNumbers(
            weight = dummyWeight,
            height = dummyHeight,
            systolic = dummySystolic,
            diastolic = dummyDiastolic,
        )
        val testObservable = observable.test()
        
        verify {
            healthNumbersLocalDataSource.saveWeight(eq(dummyWeight.toString()))
            healthNumbersLocalDataSource.saveHeight(eq(dummyHeight.toString()))
            healthNumbersLocalDataSource.saveSystolic(eq(dummySystolic.toString()))
            healthNumbersLocalDataSource.saveDiastolic(eq(dummyDiastolic.toString()))
        }
        
        testObservable.assertComplete()
        
        confirmVerified(healthNumbersLocalDataSource)
    }
    
    @Test
    fun `Clear personal data, return complete`() {
        every { healthNumbersLocalDataSource.clearData() } returns
                Completable.complete()
        
        val observable = personalDataRepository.clearHealthNumbersData()
        val testObservable = observable.test()
        
        verify {
            healthNumbersLocalDataSource.clearData()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(healthNumbersLocalDataSource)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}