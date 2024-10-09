package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: IUserRepository,
) {
    
    operator fun invoke(email: String, password: String): Flowable<Result<Nothing>> {
        return userRepository.login(
            email = email.trim(),
            password = password.trim(),
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        val user = result.data!!
                        userRepository.saveProfile(
                            patientId = user.patientId,
                            name = user.name,
                            email = user.email,
                        )
                        Result.Success(null)
                    }
                    
                    is Result.Standby -> Result.Standby
                    is Result.Loading -> Result.Loading
                    is Result.Failed -> Result.Failed
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}