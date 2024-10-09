package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class UpdateHealthNumbersUseCase @Inject constructor(
    private val healthNumbersRepository: IHealthNumbersRepository,
) {
    
    operator fun invoke(
        weight: Int?,
        height: Int?,
        systolic: Int?,
        diastolic: Int?,
    ): Completable {
        return healthNumbersRepository.saveHealthNumbers(
            weight = weight,
            height = height,
            systolic = systolic,
            diastolic = diastolic,
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}