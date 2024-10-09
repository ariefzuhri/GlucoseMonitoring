package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference.HealthNumbersPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthNumbersLocalDataSource @Inject constructor(
    private val healthNumbersPreferences: HealthNumbersPreferences,
) {
    
    fun getWeight(): Flowable<String> {
        return healthNumbersPreferences.weight
    }
    
    fun getHeight(): Flowable<String> {
        return healthNumbersPreferences.height
    }
    
    fun getSystolic(): Flowable<String> {
        return healthNumbersPreferences.systolic
    }
    
    fun getDiastolic(): Flowable<String> {
        return healthNumbersPreferences.diastolic
    }
    
    fun saveWeight(value: String): Completable {
        return healthNumbersPreferences.saveWeight(value)
    }
    
    fun saveHeight(value: String): Completable {
        return healthNumbersPreferences.saveHeight(value)
    }
    
    fun saveSystolic(value: String): Completable {
        return healthNumbersPreferences.saveSystolic(value)
    }
    
    fun saveDiastolic(value: String): Completable {
        return healthNumbersPreferences.saveDiastolic(value = value)
    }
    
    fun clearData(): Completable {
        return healthNumbersPreferences.clearData()
    }
    
}