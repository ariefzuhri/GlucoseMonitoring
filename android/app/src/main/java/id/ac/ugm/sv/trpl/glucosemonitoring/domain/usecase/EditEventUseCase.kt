package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EditEventUseCase @Inject constructor(
    private val eventRepository: IEventRepository,
    private val userRepository: IUserRepository,
) {
    
    operator fun invoke(
        id: Int,
        type: EventType,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>> {
        return userRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .take(1)
            .flatMap { profile ->
                eventRepository.editEvent(
                    patientId = profile.patientId,
                    id = id,
                    type = type.id,
                    desc = desc.trim(),
                    date = date.trim(),
                    time = time.trim(),
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}