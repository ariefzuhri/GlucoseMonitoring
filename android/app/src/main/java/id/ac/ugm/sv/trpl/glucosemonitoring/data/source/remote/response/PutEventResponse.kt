package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class PutEventResponse(
    
    @Json(name = "data")
    val data: Data? = null,
    
    @Json(name = "success")
    val success: Boolean? = null,
    
    @Json(name = "message")
    val message: String? = null,
    
    ) {
    
    @JsonClass(generateAdapter = true)
    data class Data(
        
        @Json(name = "updated_at")
        val updatedAt: String? = null,
        
        @Json(name = "ev_desc")
        val evDesc: String? = null,
        
        @Json(name = "pt_id")
        val ptId: String? = null,
        
        @Json(name = "ev_date")
        val evDate: String? = null,
        
        @Json(name = "ev_time")
        val evTime: String? = null,
        
        @Json(name = "created_at")
        val createdAt: String? = null,
        
        @Json(name = "ev_type")
        val evType: String? = null,
        
        @Json(name = "ev_id")
        val evId: Int? = null,
        
        )
    
}