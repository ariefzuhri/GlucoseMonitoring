package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.rxjava3.rxPreferencesDataStore
import androidx.datastore.rxjava3.RxDataStore
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.clearData
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.get
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.save
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastSeenPreferences @Inject constructor(
    application: Application,
) {
    
    private val dataStore = application.dataStore
    
    companion object {
        private val Context.dataStore: RxDataStore<Preferences> by rxPreferencesDataStore(
            name = "last_seen"
        )
        
        private val GLUCOSE_LEVEL = stringPreferencesKey("glucose_level")
        private val GLUCOSE_TIME = stringPreferencesKey("glucose_time")
    }
    
    val glucoseLevel: Flowable<String> = dataStore.get(GLUCOSE_LEVEL, String.EMPTY)
    val glucoseTime: Flowable<String> = dataStore.get(GLUCOSE_TIME, String.EMPTY)
    
    fun saveGlucoseLevel(value: String): Completable =
        dataStore.save(GLUCOSE_LEVEL, value)
    
    fun saveGlucoseTime(time: String): Completable =
        dataStore.save(GLUCOSE_TIME, time)
    
    fun clearData(): Completable =
        dataStore.clearData()
    
}