package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.preference.LastSeenPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastSeenLocalDataSource @Inject constructor(
    private val lastSeenPreferences: LastSeenPreferences,
) {
    
    fun getGlucoseLevel(): Flowable<String> {
        return lastSeenPreferences.glucoseLevel
    }
    
    fun getGlucoseTime(): Flowable<String> {
        return lastSeenPreferences.glucoseTime
    }
    
    fun saveGlucoseLevel(value: String): Completable {
        return lastSeenPreferences.saveGlucoseLevel(value)
    }
    
    fun saveGlucoseTime(time: String): Completable {
        return lastSeenPreferences.saveGlucoseTime(time)
    }
    
    fun clearData(): Completable {
        return lastSeenPreferences.clearData()
    }
    
}