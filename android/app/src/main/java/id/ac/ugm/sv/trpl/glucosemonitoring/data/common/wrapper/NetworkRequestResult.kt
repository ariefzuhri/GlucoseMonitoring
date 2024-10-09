package id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.ReplaySubject

abstract class NetworkRequestResult<RequestType, ResultType : Any> {
    
    private val result = ReplaySubject.create<Result<ResultType>>()
    
    private val disposable = CompositeDisposable()
    
    init {
        result.onNext(Result.Standby)
        makeNetworkRequest()
    }
    
    protected abstract fun createCall(): Flowable<ApiResponse<RequestType>>
    
    protected open fun saveCallResult(response: RequestType): Completable {
        return Completable.complete()
    }
    
    protected open fun doMapping(response: RequestType): ResultType? {
        return null
    }
    
    private fun makeNetworkRequest() {
        disposable.add(
            createCall()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                    result.onNext(Result.Loading)
                }
                .doFinally {
                    disposable.dispose()
                }
                .doOnError { throwable ->
                    throwable.printStackTrace()
                    result.onNext(Result.Failed)
                }
                .subscribe { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            storeToDatabase(response.data)
                        }
                        
                        is ApiResponse.Empty -> {
                            result.onNext(Result.Success())
                        }
                        
                        is ApiResponse.Failed -> {
                            result.onNext(Result.Failed)
                        }
                    }
                }
        )
    }
    
    private fun storeToDatabase(response: RequestType) {
        disposable.add(
            saveCallResult(response)
                .doOnError { throwable ->
                    throwable.printStackTrace()
                    result.onNext(Result.Failed)
                }
                .subscribe {
                    mapResponse(response)
                }
        )
    }
    
    private fun mapResponse(response: RequestType) {
        val mappedData = doMapping(response)
        result.onNext(Result.Success(mappedData))
    }
    
    fun asFlowable(): Flowable<Result<ResultType>> =
        result
            .doOnError { throwable ->
                throwable.printStackTrace()
                result.onNext(Result.Failed)
            }
            .toFlowable(BackpressureStrategy.BUFFER)
    
}