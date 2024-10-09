package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.network

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.*
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.*

interface ApiService {
    
    @POST("/api/login")
    @FormUrlEncoded
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Flowable<PostLoginResponse>
    
    @GET("/api/glucoses/{patient_id}")
    fun getGlucoses(
        @Path("patient_id") patientId: Int,
    ): Flowable<GetGlucosesResponse>
    
    @GET("/api/events/{patient_id}")
    fun getEvents(
        @Path("patient_id") patientId: Int,
    ): Flowable<GetEventsResponse>
    
    @POST("/api/events")
    @FormUrlEncoded
    fun postEvent(
        @Field("pt_id") patientId: Int,
        @Field("ev_type") type: String,
        @Field("ev_desc") desc: String,
        @Field("ev_date") date: String,
        @Field("ev_time") time: String,
    ): Flowable<PostEventResponse>
    
    @PUT("/api/events/{event_id}")
    fun putEvent(
        @Path("event_id") id: Int,
        @Query("pt_id") patientId: Int,
        @Query("ev_type") type: String,
        @Query("ev_desc") desc: String,
        @Query("ev_date") date: String,
        @Query("ev_time") time: String,
    ): Flowable<PutEventResponse>
    
    @DELETE("/api/events/{event_id}")
    fun deleteEvent(
        @Path("event_id") id: Int,
    ): Flowable<DeleteEventResponse>
    
}