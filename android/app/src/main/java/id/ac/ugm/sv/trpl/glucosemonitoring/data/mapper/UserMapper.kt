package id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostLoginResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.Default
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {
    
    fun mapToDomain(input: PostLoginResponse): User {
        return User(
            patientId = input.data?.ptId ?: Default.id,
            name = input.data?.name ?: Default.string,
            email = input.data?.email ?: Default.string,
        )
    }
    
}