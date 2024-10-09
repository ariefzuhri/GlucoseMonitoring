package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.EventEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao.EventDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventLocalDataSource @Inject constructor(
    private val eventDao: EventDao
) {
    
    fun getEvents(): Flowable<List<EventEntity>> {
        return eventDao.getEvents()
    }
    
    fun insertEvents(eventEntities: List<EventEntity>): Completable {
        return eventDao.insertEvents(eventEntities)
    }
    
    fun insertEvent(eventEntity: EventEntity): Completable {
        return eventDao.insertEvent(eventEntity)
    }
    
    fun updateEvent(eventEntity: EventEntity): Completable {
        return eventDao.updateEvent(eventEntity)
    }
    
    fun deleteEvent(eventEntity: EventEntity): Completable {
        return eventDao.deleteEvent(eventEntity)
    }
    
    fun deleteEvents(): Completable {
        return eventDao.deleteEvents()
    }
    
}