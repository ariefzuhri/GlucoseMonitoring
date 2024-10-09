package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Settings
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: ISettingsRepository
) {
    
    operator fun invoke(): Flowable<Settings> {
        return settingsRepository.getSettings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}