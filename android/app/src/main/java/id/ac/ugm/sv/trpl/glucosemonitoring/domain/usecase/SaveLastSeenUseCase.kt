package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SaveLastSeenUseCase @Inject constructor(
    private val lastSeenRepository: ILastSeenRepository,
) {
    
    operator fun invoke(glucoseLevel: Int?): Completable {
        return lastSeenRepository.saveLastSeen(
            glucoseLevel = glucoseLevel,
            glucoseTime = getCurrentDateTime().toString(DateFormat.RAW_FULL),
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}