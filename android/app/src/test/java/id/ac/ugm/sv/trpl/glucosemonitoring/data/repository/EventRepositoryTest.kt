package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.EventMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.EventLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.EventEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.EventRemoteDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.DeleteEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetEventsResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.rule.RxImmediateSchedulerRule
import io.mockk.Called
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

class EventRepositoryTest {
    
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    
    private lateinit var eventRepository: EventRepository
    private lateinit var eventLocalDataSource: EventLocalDataSource
    private lateinit var eventRemoteDataSource: EventRemoteDataSource
    private lateinit var eventMapper: EventMapper
    
    @Before
    fun setUp() {
        eventLocalDataSource = mockk()
        eventRemoteDataSource = mockk()
        eventMapper = mockk()
        
        eventRepository = EventRepository(
            eventLocalDataSource = eventLocalDataSource,
            eventRemoteDataSource = eventRemoteDataSource,
            eventMapper = eventMapper,
        )
    }
    
    @Test
    fun `Update event data, remote service works as expected, store result locally and return success`() {
        val dummyPatientId = 1
        val dummySuccessResponse = GetEventsResponse(
            data = listOf(
                GetEventsResponse.DataItem(
                    ptId = 1,
                    evId = 1,
                    evType = "meal",
                    evDesc = "desc",
                    evDate = "2000-01-01",
                    evTime = "00:00",
                    createdAt = "2000-01-01 00:00",
                    updatedAt = "2000-01-01 00:00",
                ),
            ),
            success = true,
            message = ""
        )
        val dummyMappedResponse = listOf(
            EventEntity(
                id = 1,
                type = "meal",
                desc = "desc",
                date = "2000-01-01",
                time = "00:00",
            ),
        )
        
        every { eventRemoteDataSource.getEvents(dummyPatientId) } returns Flowable.just(
            ApiResponse.Success(dummySuccessResponse)
        )
        every { eventMapper.mapToEntities(dummySuccessResponse) } returns
                dummyMappedResponse
        every { eventLocalDataSource.deleteEvents() } returns
                Completable.complete()
        every { eventLocalDataSource.insertEvents(ofType()) } returns
                Completable.complete()
        
        val observable = eventRepository.downloadEventData(dummyPatientId)
        val testObservable = observable.test()
        
        verifySequence {
            eventRemoteDataSource.getEvents(eq(dummyPatientId))
            eventMapper.mapToEntities(eq(dummySuccessResponse))
            eventLocalDataSource.deleteEvents()
            eventLocalDataSource.insertEvents(any())
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
            eventRemoteDataSource,
            eventMapper,
            eventLocalDataSource,
        )
    }
    
    @Test
    fun `Update event data, remote service doesn't work as expected, just return failed`() {
        val dummyPatientId = 1
        val dummyFailedResponse = ""
        
        every { eventRemoteDataSource.getEvents(dummyPatientId) } returns Flowable.just(
            ApiResponse.Failed(dummyFailedResponse)
        )
        
        val observable = eventRepository.downloadEventData(dummyPatientId)
        val testObservable = observable.test()
        
        verify {
            eventRemoteDataSource.getEvents(eq(dummyPatientId))
            eventMapper wasNot Called
            eventLocalDataSource wasNot Called
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
            eventRemoteDataSource,
            eventMapper,
            eventLocalDataSource,
        )
    }
    
    @Test
    fun `Get event data from local, return correct domain-mapped`() {
        val dummyEntities = listOf(
            EventEntity(
                id = 1,
                type = "meal",
                desc = "desc",
                date = "2000-01-01",
                time = "00:00",
            ),
        )
        val dummyDomain = listOf(
            Event(
                id = 1,
                type = EventType.MEAL,
                desc = "desc",
                date = "2000-01-01",
                time = "00:00",
            ),
        )
        
        every { eventLocalDataSource.getEvents() } returns Flowable.just(
            dummyEntities
        )
        every { eventMapper.mapToDomain(eq(dummyEntities)) } returns
                dummyDomain
        
        val observable = eventRepository.getEventData()
        val testObservable = observable.test()
        
        verifySequence {
            eventLocalDataSource.getEvents()
            eventMapper.mapToDomain(eq(dummyEntities))
            eventRemoteDataSource wasNot Called
        }
        
        testObservable.assertResult(dummyDomain)
        
        confirmVerified(
            eventLocalDataSource,
            eventMapper,
            eventRemoteDataSource,
        )
    }
    
    @Test
    fun `Add an event, remote service works as expected, also save locally and return success`() {
        val dummyPatientId = 1
        val dummyType = "meal"
        val dummyDesc = "desc"
        val dummyDate = "2000-01-01"
        val dummyTime = "00:00"
        val dummySuccessResponse = PostEventResponse(
            data = PostEventResponse.Data(
                evId = 1,
                evType = dummyType,
                evDesc = dummyDesc,
                evDate = dummyDate,
                evTime = dummyTime,
                createdAt = "2000-01-01 00:00",
                updatedAt = "2000-01-01 00:00",
            ),
            success = true,
            message = "",
        )
        val dummyEntity = EventEntity(
            id = 1,
            type = dummyType,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
        
        every {
            eventRemoteDataSource.postEvent(
                patientId = eq(dummyPatientId),
                type = eq(dummyType),
                desc = eq(dummyDesc),
                date = eq(dummyDate),
                time = eq(dummyTime),
            )
        } returns Flowable.just(
            ApiResponse.Success(dummySuccessResponse)
        )
        every { eventMapper.mapToEntity(eq(dummySuccessResponse.data!!)) } returns
                dummyEntity
        every { eventLocalDataSource.insertEvent(eq(dummyEntity)) } returns
                Completable.complete()
        
        val observable = eventRepository.addEvent(
            patientId = dummyPatientId,
            type = dummyType,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
        val testObservable = observable.test()
        
        verifySequence {
            eventRemoteDataSource.postEvent(
                patientId = eq(dummyPatientId),
                type = eq(dummyType),
                desc = eq(dummyDesc),
                date = eq(dummyDate),
                time = eq(dummyTime),
            )
            eventMapper.mapToEntity(eq(dummySuccessResponse.data!!))
            eventLocalDataSource.insertEvent(eq(dummyEntity))
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
            eventRemoteDataSource,
            eventMapper,
            eventLocalDataSource,
        )
    }
    
    @Test
    fun `Add an event, remote service doesn't work as expected, just return failed`() {
        val dummyPatientId = 1
        val dummyType = "meal"
        val dummyDesc = "desc"
        val dummyDate = "2000-01-01"
        val dummyTime = "00:00"
        val dummyFailedResponse = ""
        
        every {
            eventRemoteDataSource.postEvent(
                patientId = eq(dummyPatientId),
                type = eq(dummyType),
                desc = eq(dummyDesc),
                date = eq(dummyDate),
                time = eq(dummyTime),
            )
        } returns Flowable.just(
            ApiResponse.Failed(dummyFailedResponse)
        )
        
        val observable = eventRepository.addEvent(
            patientId = dummyPatientId,
            type = dummyType,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
        val testObservable = observable.test()
        
        verifySequence {
            eventRemoteDataSource.postEvent(
                patientId = eq(dummyPatientId),
                type = eq(dummyType),
                desc = eq(dummyDesc),
                date = eq(dummyDate),
                time = eq(dummyTime),
            )
            eventMapper wasNot Called
            eventLocalDataSource wasNot Called
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
            eventRemoteDataSource,
            eventMapper,
            eventLocalDataSource,
        )
    }
    
    @Test
    fun `Delete an event, remote service works as expected, also delete locally and return success`() {
        val dummyId = 1
        val dummyType = "meal"
        val dummyDesc = "desc"
        val dummyDate = "2000-01-01"
        val dummyTime = "00:00"
        val dummySuccessResponse = DeleteEventResponse(
            data = DeleteEventResponse.Data(
                evId = dummyId,
                evType = dummyType,
                evDesc = dummyDesc,
                evDate = dummyDate,
                evTime = dummyTime,
                createdAt = "2000-01-01 00:00",
                updatedAt = "2000-01-01 00:00",
            ),
            success = true,
            message = "",
        )
        val dummyEntity = EventEntity(
            id = dummyId,
            type = dummyType,
            desc = dummyDesc,
            date = dummyDate,
            time = dummyTime,
        )
        
        every { eventRemoteDataSource.deleteEvent(eq(dummyId)) } returns Flowable.just(
            ApiResponse.Success(dummySuccessResponse)
        )
        every { eventMapper.mapToEntity(eq(dummySuccessResponse.data!!)) } returns
                dummyEntity
        every { eventLocalDataSource.deleteEvent(eq(dummyEntity)) } returns
                Completable.complete()
        
        val observable = eventRepository.deleteEvent(
            id = dummyId,
        )
        val testObservable = observable.test()
        
        verifySequence {
            eventRemoteDataSource.deleteEvent(
                id = eq(dummyId),
            )
            eventMapper.mapToEntity(eq(dummySuccessResponse.data!!))
            eventLocalDataSource.deleteEvent(eq(dummyEntity))
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
            eventRemoteDataSource,
            eventMapper,
            eventLocalDataSource,
        )
    }
    
    @Test
    fun `Delete an event, remote service doesn't work as expected, just return failed`() {
        val dummyId = 1
        val dummyFailedResponse = ""
        
        every { eventRemoteDataSource.deleteEvent(eq(dummyId)) } returns Flowable.just(
            ApiResponse.Failed(dummyFailedResponse)
        )
        
        val observable = eventRepository.deleteEvent(
            id = dummyId,
        )
        val testObservable = observable.test()
        
        verifySequence {
            eventRemoteDataSource.deleteEvent(
                id = eq(dummyId),
            )
            eventMapper wasNot Called
            eventLocalDataSource wasNot Called
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
            eventRemoteDataSource,
            eventMapper,
            eventLocalDataSource,
        )
    }
    
    @Test
    fun `Clear event data, return complete`() {
        every { eventLocalDataSource.deleteEvents() } returns
                Completable.complete()
        
        val observable = eventRepository.clearEventData()
        val testObservable = observable.test()
        
        verifySequence {
            eventLocalDataSource.deleteEvents()
        }
        
        testObservable.assertComplete()
        
        confirmVerified(eventLocalDataSource)
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
}