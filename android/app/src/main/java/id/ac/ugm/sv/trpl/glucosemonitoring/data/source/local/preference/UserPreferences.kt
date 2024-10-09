package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.rxjava3.rxPreferencesDataStore
import androidx.datastore.rxjava3.RxDataStore
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.clearData
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.get
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.util.save
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.INVALID
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    application: Application,
) {
    
    private val dataStore = application.dataStore
    
    companion object {
        private val Context.dataStore: RxDataStore<Preferences> by rxPreferencesDataStore(
            name = "user"
        )
        
        private val PATIENT_ID = intPreferencesKey("patient_id")
        private val NAME = stringPreferencesKey("name")
        private val EMAIL = stringPreferencesKey("email")
    }
    
    val patientId: Flowable<Int> = dataStore.get(PATIENT_ID, Int.INVALID)
    val name: Flowable<String> = dataStore.get(NAME, String.EMPTY)
    val email: Flowable<String> = dataStore.get(EMAIL, String.EMPTY)
    
    fun savePatientId(id: Int): Completable =
        dataStore.save(PATIENT_ID, id)
    
    fun saveName(name: String): Completable =
        dataStore.save(NAME, name)
    
    fun saveEmail(email: String): Completable =
        dataStore.save(EMAIL, email)
    
    fun clearData(): Completable =
        dataStore.clearData()
    
}