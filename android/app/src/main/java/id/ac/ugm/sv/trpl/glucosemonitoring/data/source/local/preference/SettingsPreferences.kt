package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.rxjava3.rxPreferencesDataStore
import androidx.datastore.rxjava3.RxDataStore
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.clearData
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.get
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.save
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPreferences @Inject constructor(
    application: Application,
) {
    
    private val dataStore = application.dataStore
    
    companion object {
        private val Context.dataStore: RxDataStore<Preferences> by rxPreferencesDataStore(
            name = "settings"
        )
        
        private val ENABLE_GLUCOSE_ALARMS = booleanPreferencesKey("enable_glucose_alarms")
    }
    
    val enableGlucoseAlarms: Flowable<Boolean> =
        dataStore.get(ENABLE_GLUCOSE_ALARMS, false)
    
    fun saveEnableGlucoseAlarms(isEnabled: Boolean): Completable =
        dataStore.save(ENABLE_GLUCOSE_ALARMS, isEnabled)
    
    fun clearData(): Completable =
        dataStore.clearData()
    
}