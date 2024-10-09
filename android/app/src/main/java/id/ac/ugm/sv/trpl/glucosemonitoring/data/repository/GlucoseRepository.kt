package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.ApiResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper.NetworkRequestResult
import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.GlucoseMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.GlucoseLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.GlucoseRemoteDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetGlucosesResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlucoseRepository @Inject constructor(
    private val glucoseLocalDataSource: GlucoseLocalDataSource,
    private val glucoseRemoteDataSource: GlucoseRemoteDataSource,
    private val glucoseMapper: GlucoseMapper,
) : IGlucoseRepository {
    
    override fun downloadGlucoseData(patientId: Int): Flowable<Result<Nothing>> {
        return object : NetworkRequestResult<GetGlucosesResponse, Nothing>() {
            
            override fun createCall(): Flowable<ApiResponse<GetGlucosesResponse>> {
                return glucoseRemoteDataSource.getGlucoses(patientId)
            }
            
            override fun saveCallResult(response: GetGlucosesResponse): Completable {
                val glucoseEntities = glucoseMapper.mapToEntities(response)
                return glucoseLocalDataSource.deleteGlucoses()
                    .andThen(glucoseLocalDataSource.insertGlucoses(glucoseEntities))
            }
            
        }.asFlowable()
    }
    
    override fun monitorGlucoseData(patientId: Int): Flowable<Result<List<Glucose>>> {
        return object : NetworkRequestResult<GetGlucosesResponse, List<Glucose>>() {
            
            override fun createCall(): Flowable<ApiResponse<GetGlucosesResponse>> {
                return glucoseRemoteDataSource.getGlucoses(patientId)
            }
            
            override fun doMapping(response: GetGlucosesResponse): List<Glucose> {
                return glucoseMapper.mapToDomain(response)
            }
            
        }.asFlowable()
    }
    
    override fun getGlucoseData(): Flowable<List<Glucose>> {
        return glucoseLocalDataSource.getGlucoses()
            .observeOn(Schedulers.io())
            .map { glucoseEntities ->
                glucoseMapper.mapToDomain(glucoseEntities)
            }
    }
    
    override fun clearGlucoseData(): Completable {
        return glucoseLocalDataSource.deleteGlucoses()
    }
    
}