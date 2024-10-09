package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetHealthNumbersUseCase @Inject constructor(
    private val healthNumbersRepository: IHealthNumbersRepository,
) {
    
    operator fun invoke(): Flowable<HealthNumbers> {
        return healthNumbersRepository.getHealthNumbers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}