package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ChangeSettingsUseCase @Inject constructor(
    private val settingsRepository: ISettingsRepository,
) {
    
    operator fun invoke(enableGlucoseAlarms: Boolean): Completable {
        return settingsRepository.saveSettings(enableGlucoseAlarms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}