package id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface IGlucoseRepository {
    
    /* Remote */
    fun downloadGlucoseData(patientId: Int): Flowable<Result<Nothing>>
    
    fun monitorGlucoseData(patientId: Int): Flowable<Result<List<Glucose>>>
    
    /* Local */
    fun getGlucoseData(): Flowable<List<Glucose>>
    
    fun clearGlucoseData(): Completable
    
}