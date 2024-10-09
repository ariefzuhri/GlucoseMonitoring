package id.ac.ugm.sv.trpl.glucosemonitoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeLastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.FakeUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.data.repository.SettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class],
)
abstract class TestRepositoryModule {
    
    @Binds
    abstract fun provideEventRepository(
        eventRepository: FakeEventRepository,
    ): IEventRepository
    
    @Binds
    abstract fun provideGlucoseRepository(
        fakeGlucoseRepository: FakeGlucoseRepository,
    ): IGlucoseRepository
    
    @Binds
    abstract fun provideHealthNumbersRepository(
        fakeHealthNumbersRepository: FakeHealthNumbersRepository
    ): IHealthNumbersRepository
    
    @Binds
    abstract fun provideLastSeenRepository(
        lastSeenRepository: FakeLastSeenRepository,
    ): ILastSeenRepository
    
    @Binds
    abstract fun provideSettingsRepository(
        settingsRepository: SettingsRepository,
    ): ISettingsRepository
    
    @Binds
    abstract fun provideUserRepository(
        userRepository: FakeUserRepository,
    ): IUserRepository
    
}