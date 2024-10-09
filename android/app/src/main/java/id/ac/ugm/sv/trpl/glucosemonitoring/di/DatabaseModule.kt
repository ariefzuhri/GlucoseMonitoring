package id.ac.ugm.sv.trpl.glucosemonitoring.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.AppDatabase
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao.EventDao
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.room.dao.GlucoseDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }
    
    @Singleton
    @Provides
    fun provideEventDao(database: AppDatabase): EventDao {
        return database.eventDao()
    }
    
    @Singleton
    @Provides
    fun provideGlucoseDao(database: AppDatabase): GlucoseDao {
        return database.glucoseDao()
    }
    
}