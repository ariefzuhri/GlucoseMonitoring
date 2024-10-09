package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference.UserPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    private val userPreferences: UserPreferences,
) {
    
    fun getPatientId(): Flowable<Int> {
        return userPreferences.patientId
    }
    
    fun getName(): Flowable<String> {
        return userPreferences.name
    }
    
    fun getEmail(): Flowable<String> {
        return userPreferences.email
    }
    
    fun savePatientId(id: Int): Completable {
        return userPreferences.savePatientId(id)
    }
    
    fun saveName(name: String): Completable {
        return userPreferences.saveName(name)
    }
    
    fun saveEmail(email: String): Completable {
        return userPreferences.saveEmail(email)
    }
    
    fun clearData(): Completable {
        return userPreferences.clearData()
    }
    
}