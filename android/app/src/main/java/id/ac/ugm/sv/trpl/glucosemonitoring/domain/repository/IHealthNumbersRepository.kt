package id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface IHealthNumbersRepository {
    
    /* Local */
    fun getHealthNumbers(): Flowable<HealthNumbers>
    
    fun saveHealthNumbers(
        weight: Int?,
        height: Int?,
        systolic: Int?,
        diastolic: Int?,
    ): Completable
    
    fun clearHealthNumbersData(): Completable
    
}