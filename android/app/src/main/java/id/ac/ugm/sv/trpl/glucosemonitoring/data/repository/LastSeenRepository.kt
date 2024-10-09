package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper.LastSeenMapper
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.LastSeenLocalDataSource
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.LastSeen
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastSeenRepository @Inject constructor(
    private val lastSeenLocalDataSource: LastSeenLocalDataSource,
    private val lastSeenMapper: LastSeenMapper,
) : ILastSeenRepository {
    
    override fun getLastSeen(): Flowable<LastSeen> {
        return Flowable.combineLatest(
            lastSeenLocalDataSource.getGlucoseLevel(),
            lastSeenLocalDataSource.getGlucoseTime()
        ) { glucoseLevel, glucoseTime ->
            lastSeenMapper.mapToDomain(
                glucoseLevel = glucoseLevel,
                glucoseTime = glucoseTime,
            )
        }
    }
    
    override fun saveLastSeen(
        glucoseLevel: Int?,
        glucoseTime: String,
    ): Completable {
        return Completable.concat(
            listOf(
                lastSeenLocalDataSource.saveGlucoseLevel(glucoseLevel.toString()),
                lastSeenLocalDataSource.saveGlucoseTime(glucoseTime),
            )
        )
    }
    
    override fun clearLastSeenData(): Completable {
        return lastSeenLocalDataSource.clearData()
    }
    
}