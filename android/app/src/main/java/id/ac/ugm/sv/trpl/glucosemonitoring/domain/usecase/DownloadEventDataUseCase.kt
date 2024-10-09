package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DownloadEventDataUseCase @Inject constructor(
    private val eventRepository: IEventRepository,
    private val userRepository: IUserRepository,
) {
    
    operator fun invoke(): Flowable<Result<Nothing>> {
        return userRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .take(1)
            .flatMap { profile ->
                eventRepository.downloadEventData(profile.patientId)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}