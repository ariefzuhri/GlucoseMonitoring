package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote

import android.annotation.SuppressLint
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.network.ApiService
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.DeleteEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetEventsResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PutEventResponse
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    
    @SuppressLint("CheckResult")
    fun getEvents(patientId: Int): Flowable<ApiResponse<GetEventsResponse>> {
        val result = PublishSubject.create<ApiResponse<GetEventsResponse>>()
        
        apiService.getEvents(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.success == true && response.data != null) {
                        if (response.data.isNotEmpty()) ApiResponse.Success(response)
                        else ApiResponse.Empty
                    } else {
                        ApiResponse.Failed(response.message.orEmpty())
                    }
                )
            }, { throwable ->
                throwable.printStackTrace()
                result.onNext(
                    ApiResponse.Failed(throwable.message.orEmpty())
                )
            })
        
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
    
    @SuppressLint("CheckResult")
    fun postEvent(
        patientId: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<ApiResponse<PostEventResponse>> {
        val result = PublishSubject.create<ApiResponse<PostEventResponse>>()
        
        apiService.postEvent(
            patientId = patientId,
            type = type,
            desc = desc,
            date = date,
            time = time,
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.success == true && response.data != null) {
                        ApiResponse.Success(response)
                    } else {
                        ApiResponse.Failed(response.message.orEmpty())
                    }
                )
            }, { throwable ->
                throwable.printStackTrace()
                result.onNext(
                    ApiResponse.Failed(throwable.message.orEmpty())
                )
            })
        
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
    
    @SuppressLint("CheckResult")
    fun putEvent(
        patientId: Int,
        id: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<ApiResponse<PutEventResponse>> {
        val result = PublishSubject.create<ApiResponse<PutEventResponse>>()
        
        apiService.putEvent(
            patientId = patientId,
            id = id,
            type = type,
            desc = desc,
            date = date,
            time = time,
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.success == true && response.data != null) {
                        ApiResponse.Success(response)
                    } else {
                        ApiResponse.Failed(response.message.orEmpty())
                    }
                )
            }, { throwable ->
                throwable.printStackTrace()
                result.onNext(
                    ApiResponse.Failed(throwable.message.orEmpty())
                )
            })
        
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
    
    @SuppressLint("CheckResult")
    fun deleteEvent(id: Int): Flowable<ApiResponse<DeleteEventResponse>> {
        val result = PublishSubject.create<ApiResponse<DeleteEventResponse>>()
        
        apiService.deleteEvent(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .take(1)
            .subscribe({ response ->
                result.onNext(
                    if (response.success == true && response.data != null) {
                        ApiResponse.Success(response)
                    } else {
                        ApiResponse.Failed(response.message.orEmpty())
                    }
                )
            }, { throwable ->
                throwable.printStackTrace()
                result.onNext(
                    ApiResponse.Failed(throwable.message.orEmpty())
                )
            })
        
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
    
}