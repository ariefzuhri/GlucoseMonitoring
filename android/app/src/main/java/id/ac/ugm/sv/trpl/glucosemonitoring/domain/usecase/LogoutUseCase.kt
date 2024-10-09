package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ISettingsRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val glucoseRepository: IGlucoseRepository,
    private val eventRepository: IEventRepository,
    private val iHealthNumbersRepository: IHealthNumbersRepository,
    private val lastSeenRepository: ILastSeenRepository,
    private val settingsRepository: ISettingsRepository,
) {
    
    operator fun invoke(): Completable {
        return Completable.concat(
            listOf(
                userRepository.clearUserData(),
                glucoseRepository.clearGlucoseData(),
                eventRepository.clearEventData(),
                iHealthNumbersRepository.clearHealthNumbersData(),
                lastSeenRepository.clearLastSeenData(),
                settingsRepository.resetSettings(),
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}