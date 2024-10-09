package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.NetworkRequestResult
import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.EventMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.EventLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.EventRemoteDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.DeleteEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetEventsResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PutEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventLocalDataSource: EventLocalDataSource,
    private val eventRemoteDataSource: EventRemoteDataSource,
    private val eventMapper: EventMapper,
) : IEventRepository {
    
    override fun downloadEventData(patientId: Int): Flowable<Result<Nothing>> {
        return object : NetworkRequestResult<GetEventsResponse, Nothing>() {
            
            override fun createCall(): Flowable<ApiResponse<GetEventsResponse>> {
                return eventRemoteDataSource.getEvents(patientId)
            }
            
            override fun saveCallResult(response: GetEventsResponse): Completable {
                val events = eventMapper.mapToEntities(response)
                return eventLocalDataSource.deleteEvents()
                    .andThen(eventLocalDataSource.insertEvents(events))
            }
            
        }.asFlowable()
    }
    
    override fun getEventData(): Flowable<List<Event>> {
        return eventLocalDataSource.getEvents()
            .observeOn(Schedulers.io())
            .map { eventEntities ->
                eventMapper.mapToDomain(eventEntities)
            }
    }
    
    override fun addEvent(
        patientId: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>> {
        return object : NetworkRequestResult<PostEventResponse, Nothing>() {
            
            override fun createCall(): Flowable<ApiResponse<PostEventResponse>> {
                return eventRemoteDataSource.postEvent(
                    patientId = patientId,
                    type = type,
                    desc = desc,
                    date = date,
                    time = time,
                )
            }
            
            override fun saveCallResult(response: PostEventResponse): Completable {
                val eventEntity = eventMapper.mapToEntity(response.data!!)
                return eventLocalDataSource.insertEvent(eventEntity)
            }
            
        }.asFlowable()
    }
    
    override fun editEvent(
        patientId: Int,
        id: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>> {
        return object : NetworkRequestResult<PutEventResponse, Nothing>() {
            
            override fun createCall(): Flowable<ApiResponse<PutEventResponse>> {
                return eventRemoteDataSource.putEvent(
                    patientId = patientId,
                    id = id,
                    type = type,
                    desc = desc,
                    date = date,
                    time = time,
                )
            }
            
            override fun saveCallResult(response: PutEventResponse): Completable {
                val eventEntity = eventMapper.mapToEntity(response.data!!)
                return eventLocalDataSource.updateEvent(eventEntity)
            }
            
        }.asFlowable()
    }
    
    override fun deleteEvent(id: Int): Flowable<Result<Nothing>> {
        return object : NetworkRequestResult<DeleteEventResponse, Nothing>() {
            
            override fun createCall(): Flowable<ApiResponse<DeleteEventResponse>> {
                return eventRemoteDataSource.deleteEvent(id)
            }
            
            override fun saveCallResult(response: DeleteEventResponse): Completable {
                val eventEntity = eventMapper.mapToEntity(response.data!!)
                return eventLocalDataSource.deleteEvent(eventEntity)
            }
            
        }.asFlowable()
    }
    
    override fun clearEventData(): Completable {
        return eventLocalDataSource.deleteEvents()
    }
    
}