package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class FakeEventRepository : IEventRepository {
    
    private var dummyEventData = mutableListOf<Event>()
    
    override fun downloadEventData(patientId: Int): Flowable<Result<Nothing>> {
        dummyEventData = DummyData.allEvents.toMutableList()
        return Flowable.just(Result.Success(null))
    }
    
    override fun getEventData(): Flowable<List<Event>> {
        return Flowable.just(dummyEventData)
    }
    
    override fun addEvent(
        patientId: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>> {
        val event = Event(
            id = dummyEventData.size + 1,
            type = EventType.of(type),
            desc = desc,
            date = date,
            time = time,
        )
        dummyEventData.add(event)
        return Flowable.just(Result.Success(null))
    }
    
    override fun editEvent(
        patientId: Int,
        id: Int,
        type: String,
        desc: String,
        date: String,
        time: String,
    ): Flowable<Result<Nothing>> {
        val index = dummyEventData.indexOfFirst { it.id == id }
        val updatedEvent = dummyEventData[index].copy(
            type = EventType.of(type),
            desc = desc,
            date = date,
            time = time,
        )
        dummyEventData[index] = updatedEvent
        return Flowable.just(Result.Success(null))
    }
    
    override fun deleteEvent(id: Int): Flowable<Result<Nothing>> {
        dummyEventData.removeIf { it.id == id }
        return Flowable.just(Result.Success(null))
    }
    
    override fun clearEventData(): Completable {
        dummyEventData.clear()
        return Completable.complete()
    }
    
}