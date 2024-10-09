package id.ac.ugm.sv.trpl.glucosemonitoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.EventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.GlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.HealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.LastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.SettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.UserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository

@Module(includes = [DatabaseModule::class, NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun provideEventRepository(
        eventRepository: EventRepository,
    ): IEventRepository
    
    @Binds
    abstract fun provideGlucoseRepository(
        glucoseRepository: GlucoseRepository,
    ): IGlucoseRepository
    
    @Binds
    abstract fun provideHealthNumbersRepository(
        healthNumbersRepository: HealthNumbersRepository
    ): IHealthNumbersRepository
    
    @Binds
    abstract fun provideLastSeenRepository(
        lastSeenRepository: LastSeenRepository,
    ): ILastSeenRepository
    
    @Binds
    abstract fun provideSettingsRepository(
        settingsRepository: SettingsRepository,
    ): ISettingsRepository
    
    @Binds
    abstract fun provideUserRepository(
        userRepository: UserRepository,
    ): IUserRepository
    
}