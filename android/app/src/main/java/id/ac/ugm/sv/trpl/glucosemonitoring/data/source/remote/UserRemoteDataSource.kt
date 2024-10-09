package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote

import android.annotation.SuppressLint
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.network.ApiService
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostLoginResponse
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    
    @SuppressLint("CheckResult")
    fun postLogin(email: String, password: String): Flowable<ApiResponse<PostLoginResponse>> {
        val result = PublishSubject.create<ApiResponse<PostLoginResponse>>()
        
        apiService.postLogin(
            email = email,
            password = password,
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
    
}