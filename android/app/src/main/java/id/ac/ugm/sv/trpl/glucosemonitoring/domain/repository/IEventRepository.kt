package id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface IEventRepository {
    
    /* Remote */
    fun downloadEventData(patientId: Int): Flowable<Result<Nothing>>
    
    /* Local */
    fun getEventData(): Flowable<List<Event>>
    
    /* Remote + Local */
    fun addEvent(
        patientId: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>>
    
    fun editEvent(
        patientId: Int,
        id: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>>
    
    fun deleteEvent(id: Int): Flowable<Result<Nothing>>
    
    fun clearEventData(): Completable
    
}