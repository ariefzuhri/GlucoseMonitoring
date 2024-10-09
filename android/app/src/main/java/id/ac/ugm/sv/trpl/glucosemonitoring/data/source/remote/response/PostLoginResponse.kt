package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class PostLoginResponse(
    
    @Json(name = "data")
    val data: Data? = null,
    
    @Json(name = "success")
    val success: Boolean? = null,
    
    @Json(name = "message")
    val message: String? = null,
    
    ) {
    
    @JsonClass(generateAdapter = true)
    data class Data(
        
        @Json(name = "is_admin")
        val isAdmin: Int? = null,
        
        @Json(name = "address")
        val address: Any? = null,
        
        @Json(name = "created_on")
        val createdOn: String? = null,
        
        @Json(name = "pt_id")
        val ptId: Int? = null,
        
        @Json(name = "name")
        val name: String? = null,
        
        @Json(name = "photo")
        val photo: Any? = null,
        
        @Json(name = "company")
        val company: Any? = null,
        
        @Json(name = "id")
        val id: Int? = null,
        
        @Json(name = "fullname")
        val fullname: Any? = null,
        
        @Json(name = "photos")
        val photos: Any? = null,
        
        @Json(name = "email")
        val email: String? = null,
        
        @Json(name = "status")
        val status: Int? = null,
        
        )
    
}