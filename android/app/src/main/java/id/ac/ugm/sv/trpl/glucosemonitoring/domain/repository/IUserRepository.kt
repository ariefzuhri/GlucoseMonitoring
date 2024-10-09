package id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface IUserRepository {
    
    /* Remote */
    fun login(email: String, password: String): Flowable<Result<User>>
    
    /* Local */
    fun getProfile(): Flowable<User>
    
    fun saveProfile(
        patientId: Int,
        name: String,
        email: String,
    ): Completable
    
    fun clearUserData(): Completable
    
}