package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference.SettingsPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsLocalDataSource @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
) {
    
    fun getEnableGlucoseAlarms(): Flowable<Boolean> {
        return settingsPreferences.enableGlucoseAlarms
    }
    
    fun saveEnableGlucoseAlarms(isEnabled: Boolean): Completable {
        return settingsPreferences.saveEnableGlucoseAlarms(isEnabled)
    }
    
    fun clearData(): Completable {
        return settingsPreferences.clearData()
    }
    
}