package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.INVALID
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.User
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.util.Constants
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : IUserRepository {
    
    private var patientId: Int = Int.INVALID
    private var name: String = String.EMPTY
    private var email: String = String.EMPTY
    
    // Sebenarnya bisa ditiap setUp
    init {
        patientId = Constants.DummyPatientID.DEFAULT
        name = "John Doe"
        email = "johndoe@mail.com"
    }
    
    override fun login(email: String, password: String): Flowable<Result<User>> {
        val name = "John Doe"
        val user = User(
            patientId = Constants.DummyPatientID.DEFAULT,
            name = name,
            email = email,
        )
        
        this.patientId = user.patientId
        this.name = user.name
        this.email = user.email
        
        return Flowable.just(Result.Success(user))
    }
    
    override fun getProfile(): Flowable<User> {
        val user = User(
            patientId = patientId,
            name = name,
            email = email,
        )
        return Flowable.just(user)
    }
    
    override fun saveProfile(patientId: Int, name: String, email: String): Completable {
        this.patientId = patientId
        this.name = name
        this.email = email
        return Completable.complete()
    }
    
    
    override fun clearUserData(): Completable {
        patientId = Int.INVALID
        name = String.EMPTY
        email = String.EMPTY
        
        return Completable.complete()
    }
    
}