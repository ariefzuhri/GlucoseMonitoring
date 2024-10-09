package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class FakeGlucoseRepository @Inject constructor() : IGlucoseRepository {
    
    private var dummyGlucoseData = mutableListOf<Glucose>()
    
    init {
        dummyGlucoseData = DummyData.allGlucoses.toMutableList()
    }
    
    override fun downloadGlucoseData(patientId: Int): Flowable<Result<Nothing>> {
        dummyGlucoseData = DummyData.allGlucoses.toMutableList()
        return Flowable.just(Result.Success(null))
    }
    
    override fun monitorGlucoseData(patientId: Int): Flowable<Result<List<Glucose>>> {
        return Flowable.just(Result.Success(DummyData.allGlucoses.toMutableList()))
    }
    
    override fun getGlucoseData(): Flowable<List<Glucose>> {
        return Flowable.just(dummyGlucoseData)
    }
    
    override fun clearGlucoseData(): Completable {
        dummyGlucoseData.clear()
        return Completable.complete()
    }
    
}