package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao

import androidx.room.*
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.GlucoseEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface GlucoseDao {
    
    @Query("SELECT * FROM glucoses")
    fun getGlucoses(): Flowable<List<GlucoseEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGlucoses(glucoseEntities: List<GlucoseEntity>): Completable
    
    @Query("DELETE FROM glucoses")
    fun deleteGlucoses(): Completable
    
}