package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class FakeHealthNumbersRepository @Inject constructor() : IHealthNumbersRepository {
    
    private var weight: Int? = null
    private var height: Int? = null
    private var systolic: Int? = null
    private var diastolic: Int? = null
    
    init {
        weight = DummyData.HealthNumbers.WEIGHT
        height = DummyData.HealthNumbers.HEIGHT
        systolic = DummyData.HealthNumbers.SYSTOLIC
        diastolic = DummyData.HealthNumbers.DIASTOLIC
    }
    
    override fun getHealthNumbers(): Flowable<HealthNumbers> {
        val healthNumbers = HealthNumbers(
            weight = weight,
            height = height,
            systolic = systolic,
            diastolic = diastolic,
        )
        return Flowable.just(healthNumbers)
    }
    
    override fun saveHealthNumbers(
        weight: Int?,
        height: Int?,
        systolic: Int?,
        diastolic: Int?,
    ): Completable {
        this.weight = weight
        this.height = height
        this.systolic = systolic
        this.diastolic = diastolic
        return Completable.complete()
    }
    
    override fun clearHealthNumbersData(): Completable {
        weight = null
        height = null
        systolic = null
        diastolic = null
        
        return Completable.complete()
    }
    
}