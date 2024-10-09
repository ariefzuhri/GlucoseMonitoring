package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao

import androidx.room.*
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.EventEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events")
    fun getEvents(): Flowable<List<EventEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvents(eventEntities: List<EventEntity>): Completable
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(eventEntity: EventEntity): Completable
    
    @Update
    fun updateEvent(eventEntity: EventEntity): Completable
    
    @Delete
    fun deleteEvent(eventEntity: EventEntity): Completable
    
    @Query("DELETE FROM events")
    fun deleteEvents(): Completable
    
}