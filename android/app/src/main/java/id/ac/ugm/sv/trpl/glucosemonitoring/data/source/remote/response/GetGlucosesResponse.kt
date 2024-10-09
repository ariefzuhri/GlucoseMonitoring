package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetGlucosesResponse(
    
    @Json(name = "data")
    val data: List<DataItem?>? = null,
    
    @Json(name = "success")
    val success: Boolean? = null,
    
    @Json(name = "message")
    val message: String? = null,
    
    ) {
    
    @JsonClass(generateAdapter = true)
    data class DataItem(
        
        @Json(name = "bg_time")
        val bgTime: String? = null,
        
        @Json(name = "date_time")
        val dateTime: String? = null,
        
        @Json(name = "pt_id")
        val ptId: Int? = null,
        
        @Json(name = "file_type")
        val fileType: String? = null,
        
        @Json(name = "bg_date")
        val bgDate: String? = null,
        
        @Json(name = "bg_level")
        val bgLevel: Float? = null,
        
        @Json(name = "rec_id")
        val recId: Int? = null,
        
        @Json(name = "calibration")
        val calibration: String? = null,
        
        )
    
}