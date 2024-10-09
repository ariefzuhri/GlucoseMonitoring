package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.EventEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.GlucoseEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao.EventDao
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao.GlucoseDao

@Database(
    entities = [
        EventEntity::class,
        GlucoseEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    
    companion object {
        const val DATABASE_NAME = "glucover.db"
    }
    
    abstract fun glucoseDao(): GlucoseDao
    
    abstract fun eventDao(): EventDao
    
}