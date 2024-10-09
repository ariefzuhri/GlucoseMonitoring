package id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface ISettingsRepository {
    
    /* Local */
    fun getSettings(): Flowable<Settings>
    
    fun saveSettings(
        enableGlucoseAlarms: Boolean,
    ): Completable
    
    fun resetSettings(): Completable
    
}