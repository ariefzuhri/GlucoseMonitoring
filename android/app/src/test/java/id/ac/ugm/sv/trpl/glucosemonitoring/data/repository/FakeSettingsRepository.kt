package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class FakeSettingsRepository : ISettingsRepository {
    
    private var enableGlucoseAlarms: Boolean = false
    
    override fun getSettings(): Flowable<Settings> {
        val settings = Settings(
            enableGlucoseAlarms = enableGlucoseAlarms,
        )
        return Flowable.just(settings)
    }
    
    override fun saveSettings(enableGlucoseAlarms: Boolean): Completable {
        this.enableGlucoseAlarms = enableGlucoseAlarms
        return Completable.complete()
    }
    
    override fun resetSettings(): Completable {
        enableGlucoseAlarms = false
        
        return Completable.complete()
    }
    
}