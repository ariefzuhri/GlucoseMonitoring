package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.LastSeen
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.ILastSeenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class FakeLastSeenRepository @Inject constructor() : ILastSeenRepository {
    
    private var glucoseLevel: Int? = null
    private var glucoseTime: String? = null
    
    override fun getLastSeen(): Flowable<LastSeen> {
        val lastSeen = LastSeen(
            glucoseLevel = glucoseLevel,
            glucoseTime = glucoseTime,
        )
        return Flowable.just(lastSeen)
    }
    
    override fun saveLastSeen(glucoseLevel: Int?, glucoseTime: String): Completable {
        this.glucoseLevel = glucoseLevel
        this.glucoseTime = glucoseTime
        return Completable.complete()
    }
    
    override fun clearLastSeenData(): Completable {
        glucoseLevel = null
        glucoseTime = null
        
        return Completable.complete()
    }
    
}