package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.HealthNumbersMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.HealthNumbersLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IHealthNumbersRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthNumbersRepository @Inject constructor(
    private val healthNumbersLocalDataSource: HealthNumbersLocalDataSource,
    private val healthNumbersMapper: HealthNumbersMapper,
) : IHealthNumbersRepository {
    
    override fun getHealthNumbers(): Flowable<HealthNumbers> {
        return Flowable.combineLatest(
            healthNumbersLocalDataSource.getWeight(),
            healthNumbersLocalDataSource.getHeight(),
            healthNumbersLocalDataSource.getSystolic(),
            healthNumbersLocalDataSource.getDiastolic(),
        ) { weight, height, systolic, diastolic ->
            healthNumbersMapper.mapToDomain(
                weight = weight,
                height = height,
                systolic = systolic,
                diastolic = diastolic,
            )
        }
    }
    
    override fun saveHealthNumbers(
        weight: Int?,
        height: Int?,
        systolic: Int?,
        diastolic: Int?,
    ): Completable {
        return Completable.concat(
            listOf(
                healthNumbersLocalDataSource.saveWeight(weight.toString()),
                healthNumbersLocalDataSource.saveHeight(height.toString()),
                healthNumbersLocalDataSource.saveSystolic(systolic.toString()),
                healthNumbersLocalDataSource.saveDiastolic(diastolic.toString()),
            )
        )
    }
    
    override fun clearHealthNumbersData(): Completable {
        return healthNumbersLocalDataSource.clearData()
    }
    
}