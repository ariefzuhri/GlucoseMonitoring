package id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.LastSeen
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface ILastSeenRepository {
    
    /* Local */
    fun getLastSeen(): Flowable<LastSeen>
    
    fun saveLastSeen(
        glucoseLevel: Int?,
        glucoseTime: String,
    ): Completable
    
    fun clearLastSeenData(): Completable
    
}