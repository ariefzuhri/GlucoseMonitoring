package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.GlucoseMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.GlucoseLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.GlucoseEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.GlucoseRemoteDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetGlucosesResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GlucoseRepositoryTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var glucoseRepository: GlucoseRepository
    private lateinit var glucoseLocalDataSource: GlucoseLocalDataSource
    private lateinit var glucoseRemoteDataSource: GlucoseRemoteDataSource
    private lateinit var glucoseMapper: GlucoseMapper
    
    @Before
    fun setUp() {
        glucoseLocalDataSource = mockk()
        glucoseRemoteDataSource = mockk()
        glucoseMapper = mockk()
        
        glucoseRepository = GlucoseRepository(
            glucoseLocalDataSource = glucoseLocalDataSource,
            glucoseRemoteDataSource = glucoseRemoteDataSource,
            glucoseMapper = glucoseMapper,
        )
    }
    
    @Test
    fun `Update glucose data, remote service works as expected, store result locally and return success`() {
        val dummyPatientId = 1
        val dummySuccessResponse = GetGlucosesResponse(
            data = listOf(
                GetGlucosesResponse.DataItem(
                    ptId = dummyPatientId,
                    recId = 1,
                    bgLevel = 100f,
                    bgDate = "2000-01-01",
                    bgTime = "07:00",
                    dateTime = "2000-01-01 07:00",
                    calibration = null,
                    fileType = null,
                ),
            ),
        )
        val dummyEntities = listOf(
            GlucoseEntity(
                id = 1,
                date = "2000-01-01",
                time = "07:00",
                level = 100f,
            ),
        )
        
        every { glucoseRemoteDataSource.getGlucoses(dummyPatientId) } returns Flowable.just(
            ApiResponse.Success(dummySuccessResponse)
        )
        every { glucoseMapper.mapToEntities(dummySuccessResponse) } returns
                dummyEntities
        every { glucoseLocalDataSource.deleteGlucoses() } returns
                Completable.complete()
        every { glucoseLocalDataSource.insertGlucoses(dummyEntities) } returns
                Completable.complete()
        
        val observable = glucoseRepository.downloadGlucoseData(dummyPatientId)
        val testObservable = observable.test()
        
        verifySequence {
            glucoseRemoteDataSource.getGlucoses(eq(dummyPatientId))
            glucoseMapper.mapToEntities(eq(dummySuccessResponse))
            glucoseLocalDataSource.deleteGlucoses()
            glucoseLocalDataSource.insertGlucoses(eq(dummyEntities))
        }
        
        val expectedResultEmittedCount = 3
        testObservable.assertValueCount(expectedResultEmittedCount)
        
        val expectedFirstResult = Result.Standby
        testObservable.assertValueAt(0, expectedFirstResult)
        
        val expectedSecondResult = Result.Loading
        testObservable.assertValueAt(1, expectedSecondResult)
        
        val expectedThirdResult = Result.Success(null)
        testObservable.assertValueAt(2, expectedThirdResult)
        
        confirmVerified(
            glucoseRemoteDataSource,
            glucoseMapper,
            glucoseLocalDataSource,
        )
    }
    
    @Test
    fun `Update glucose data, remote service doesn't works as expected, just return failed`() {
        val dummyPatientId = 1
        val dummyFailedResponse = ""
        
        every { glucoseRemoteDataSource.getGlucoses(dummyPatientId) } returns Flowable.just(
            ApiResponse.Failed(dummyFailedResponse)
        )
        
        val observable = glucoseRepository.downloadGlucoseData(dummyPatientId)
        val testObservable = observable.test()
        
        verifySequence {
            glucoseRemoteDataSource.getGlucoses(eq(dummyPatientId))
            glucoseMapper wasNot Called
            glucoseLocalDataSource wasNot Called
        }
        
        val expectedResultEmittedCount = 3
        testObservable.assertValueCount(expectedResultEmittedCount)
        
        val expectedFirstResult = Result.Standby
        testObservable.assertValueAt(0, expectedFirstResult)
        
        val expectedSecondResult = Result.Loading
        testObservable.assertValueAt(1, expectedSecondResult)
        
        val expectedThirdResult = Result.Failed
        testObservable.assertValueAt(2, expectedThirdResult)
        
        confirmVerified(
            glucoseRemoteDataSource,
            glucoseMapper,
            glucoseLocalDataSource,
        )
    }
    
    @Test
    fun `Monitor glucose data, remote service works as expected, return success with data`() {
        val dummyPatientId = 1
        val dummySuccessResponse = GetGlucosesResponse(
            data = listOf(
                GetGlucosesResponse.DataItem(
                    ptId = dummyPatientId,
                    recId = 1,
                    bgLevel = 100f,
                    bgDate = "2000-01-01",
                    bgTime = "07:00",
                    dateTime = "2000-01-01 07:00",
                    calibration = null,
                    fileType = null,
                ),
            ),
        )
        val dummyDomain = listOf(
            Glucose(
                id = 1,
                date = "2000-01-01",
                time = "07:00",
                level = 100f,
            ),
        )
        
        every { glucoseRemoteDataSource.getGlucoses(dummyPatientId) } returns Flowable.just(
            ApiResponse.Success(dummySuccessResponse)
        )
        every { glucoseMapper.mapToDomain(dummySuccessResponse) } returns
                dummyDomain
        
        val observable = glucoseRepository.monitorGlucoseData(dummyPatientId)
        val testObservable = observable.test()
        
        verifySequence {
            glucoseRemoteDataSource.getGlucoses(eq(dummyPatientId))
            glucoseMapper.mapToDomain(eq(dummySuccessResponse))
            glucoseLocalDataSource wasNot Called
        }
        
        val expectedResultEmittedCount = 3
        testObservable.assertValueCount(expectedResultEmittedCount)
        
        val expectedFirstResult = Result.Standby
        testObservable.assertValueAt(0, expectedFirstResult)
        
        val expectedSecondResult = Result.Loading
        testObservable.assertValueAt(1, expectedSecondResult)
        
        val expectedThirdResult = Result.Success(dummyDomain)
        testObservable.assertValueAt(2, expectedThirdResult)
        
        confirmVerified(
            glucoseRemoteDataSource,
            glucoseMapper,
            glucoseLocalDataSource,
        )
    }
    
    @Test
    fun `Monitor glucose data, remote service doesn't work as expected, return failed`() {
        val dummyPatientId = 1
        val dummyFailedResponse = ""
        
        every { glucoseRemoteDataSource.getGlucoses(dummyPatientId) } returns Flowable.just(
            ApiResponse.Failed(dummyFailedResponse)
        )
        
        val observable = glucoseRepository.monitorGlucoseData(dummyPatientId)
        val testObservable = observable.test()
        
        verifySequence {
            glucoseRemoteDataSource.getGlucoses(eq(dummyPatientId))
            glucoseMapper wasNot Called
            glucoseLocalDataSource wasNot Called
        }
        
        val expectedResultEmittedCount = 3
        testObservable.assertValueCount(expectedResultEmittedCount)
        
        val expectedFirstResult = Result.Standby
        testObservable.assertValueAt(0, expectedFirstResult)
        
        val expectedSecondResult = Result.Loading
        testObservable.assertValueAt(1, expectedSecondResult)
        
        val expectedThirdResult = Result.Failed
        testObservable.assertValueAt(2, expectedThirdResult)
        
        confirmVerified(
            glucoseRemoteDataSource,
            glucoseMapper,
            glucoseLocalDataSource,
        )
    }
    
    @Test
    fun `Get glucose data from local, return correct domain-mapped`() {
        val dummyEntities = listOf(
            GlucoseEntity(
                id = 1,
                date = "2000-01-01",
                time = "07:00",
                level = 100f,
            ),
        )
        val dummyDomain = listOf(
            Glucose(
                id = 1,
                date = "2000-01-01",
                time = "07:00",
                level = 100f,
            ),
        )
        
        every { glucoseLocalDataSource.getGlucoses() } returns Flowable.just(
            dummyEntities
        )
        every { glucoseMapper.mapToDomain(eq(dummyEntities)) } returns
                dummyDomain
        
        val observable = glucoseRepository.getGlucoseData()
        val testObservable = observable.test()
        
        verifySequence {
            glucoseLocalDataSource.getGlucoses()
            glucoseMapper.mapToDomain(eq(dummyEntities))
            glucoseRemoteDataSource wasNot Called
        }
        
        testObservable.assertResult(dummyDomain)
        
        confirmVerified(
            glucoseLocalDataSource,
            glucoseMapper,
            glucoseRemoteDataSource,
        )
    }
    
    @Test
    fun `Clear glucose data, return complete`() {
        every { glucoseLocalDataSource.deleteGlucoses() } returns
                Completable.complete()
        
        val observable = glucoseRepository.clearGlucoseData()
        val testObservable = observable.test()
        
        verifySequence {
            glucoseLocalDataSource.deleteGlucoses()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(glucoseLocalDataSource)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}