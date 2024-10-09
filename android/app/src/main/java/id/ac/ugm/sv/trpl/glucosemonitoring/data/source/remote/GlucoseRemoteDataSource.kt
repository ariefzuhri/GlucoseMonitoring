package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote

import android.annotation.SuppressLint
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.network.ApiService
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetGlucosesResponse
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlucoseRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    
    @SuppressLint("CheckResult")
    fun getGlucoses(patientId: Int): Flowable<ApiResponse<GetGlucosesResponse>> {
        val result = PublishSubject.create<ApiResponse<GetGlucosesResponse>>()
        
        apiService.getGlucoses(patientId)
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
    
}