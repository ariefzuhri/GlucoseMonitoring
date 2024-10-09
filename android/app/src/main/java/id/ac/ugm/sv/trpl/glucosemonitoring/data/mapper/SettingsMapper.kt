package id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsMapper @Inject constructor() {
    
    fun mapToDomain(enableGlucoseAlarms: Boolean): Settings {
        return Settings(
            enableGlucoseAlarms = enableGlucoseAlarms
        )
    }
    
}