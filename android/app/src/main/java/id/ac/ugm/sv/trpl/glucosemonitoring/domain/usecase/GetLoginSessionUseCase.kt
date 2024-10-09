package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetLoginSessionUseCase @Inject constructor(
    private val userRepository: IUserRepository,
) {
    
    operator fun invoke(): Flowable<Boolean> {
        return userRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { profile ->
                profile.name.isNotEmpty()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}