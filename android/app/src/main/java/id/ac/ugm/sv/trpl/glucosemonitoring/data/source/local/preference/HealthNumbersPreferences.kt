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
class HealthNumbersPreferences @Inject constructor(
    application: Application,
) {
    
    private val dataStore = application.dataStore
    
    companion object {
        private val Context.dataStore: RxDataStore<Preferences> by rxPreferencesDataStore(
            name = "health_numbers"
        )
        
        private val WEIGHT = stringPreferencesKey("weight")
        private val HEIGHT = stringPreferencesKey("height")
        private val SYSTOLIC = stringPreferencesKey("systolic")
        private val DIASTOLIC = stringPreferencesKey("diastolic")
    }
    
    val weight: Flowable<String> = dataStore.get(key = WEIGHT, defaultValue = String.EMPTY)
    val height: Flowable<String> = dataStore.get(key = HEIGHT, defaultValue = String.EMPTY)
    val systolic: Flowable<String> = dataStore.get(key = SYSTOLIC, defaultValue = String.EMPTY)
    val diastolic: Flowable<String> = dataStore.get(key = DIASTOLIC, defaultValue = String.EMPTY)
    
    fun saveWeight(value: String): Completable =
        dataStore.save(key = WEIGHT, value)
    
    fun saveHeight(value: String): Completable =
        dataStore.save(key = HEIGHT, value)
    
    fun saveSystolic(value: String): Completable =
        dataStore.save(key = SYSTOLIC, value)
    
    fun saveDiastolic(value: String): Completable =
        dataStore.save(key = DIASTOLIC, value)
    
    fun clearData(): Completable =
        dataStore.clearData()
    
}