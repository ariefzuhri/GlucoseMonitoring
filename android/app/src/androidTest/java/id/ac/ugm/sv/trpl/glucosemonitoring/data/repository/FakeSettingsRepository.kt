package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.ReplaySubject
import javax.inject.Inject

class FakeSettingsRepository @Inject constructor() : ISettingsRepository {
    
    private val subject = ReplaySubject.create<Settings>()
    private var enableGlucoseAlarms: Boolean = false
    
    override fun getSettings(): Flowable<Settings> {
        val settings = Settings(
            enableGlucoseAlarms = enableGlucoseAlarms,
        )
        subject.onNext(settings)
        return subject.toFlowable(BackpressureStrategy.BUFFER)
    }
    
    override fun saveSettings(enableGlucoseAlarms: Boolean): Completable {
        this.enableGlucoseAlarms = enableGlucoseAlarms
        val settings = Settings(
            enableGlucoseAlarms = enableGlucoseAlarms,
        )
        subject.onNext(settings)
        return Completable.complete()
    }
    
    override fun resetSettings(): Completable {
        enableGlucoseAlarms = false
    
        val settings = Settings(
            enableGlucoseAlarms = enableGlucoseAlarms,
        )
        subject.onNext(settings)
        return Completable.complete()
    }
    
}