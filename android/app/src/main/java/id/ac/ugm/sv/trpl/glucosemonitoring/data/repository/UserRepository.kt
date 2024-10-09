package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.NetworkRequestResult
import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.UserMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.UserLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.UserRemoteDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostLoginResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.User
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IUserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userMapper: UserMapper,
) : IUserRepository {
    
    override fun login(email: String, password: String): Flowable<Result<User>> {
        return object : NetworkRequestResult<PostLoginResponse, User>() {
            
            override fun createCall(): Flowable<ApiResponse<PostLoginResponse>> {
                return userRemoteDataSource.postLogin(
                    email = email,
                    password = password,
                )
            }
            
            override fun doMapping(response: PostLoginResponse): User {
                return userMapper.mapToDomain(response)
            }
            
        }.asFlowable()
    }
    
    override fun getProfile(): Flowable<User> {
        return Flowable.combineLatest(
            userLocalDataSource.getPatientId(),
            userLocalDataSource.getName(),
            userLocalDataSource.getEmail(),
        ) { patientId, userName, userEmail ->
            User(
                patientId = patientId,
                name = userName,
                email = userEmail,
            )
        }
    }
    
    override fun saveProfile(
        patientId: Int,
        name: String,
        email: String,
    ): Completable {
        return Completable.concat(
            listOf(
                userLocalDataSource.savePatientId(patientId),
                userLocalDataSource.saveName(name),
                userLocalDataSource.saveEmail(email),
            )
        )
    }
    
    override fun clearUserData(): Completable {
        return userLocalDataSource.clearData()
    }
    
}