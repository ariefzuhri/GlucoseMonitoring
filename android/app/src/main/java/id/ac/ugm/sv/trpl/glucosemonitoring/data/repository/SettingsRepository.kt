package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.SettingsMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.SettingsLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
    private val settingsMapper: SettingsMapper,
) : ISettingsRepository {
    
    override fun getSettings(): Flowable<Settings> {
        return settingsLocalDataSource.getEnableGlucoseAlarms()
            .observeOn(Schedulers.io())
            .map { enableGlucoseAlarms ->
                settingsMapper.mapToDomain(enableGlucoseAlarms)
            }
    }
    
    override fun saveSettings(enableGlucoseAlarms: Boolean): Completable {
        return settingsLocalDataSource.saveEnableGlucoseAlarms(enableGlucoseAlarms)
    }
    
    override fun resetSettings(): Completable {
        return settingsLocalDataSource.clearData()
    }
    
}