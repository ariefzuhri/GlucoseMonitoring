package id.ac.ugm.sv.trpl.glucosemonitoring.data.repository

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.util.DummyData
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.ReplaySubject
import javax.inject.Inject

class FakeEventRepository @Inject constructor() : IEventRepository {
    
    private val subject = ReplaySubject.create<List<Event>>()
    private var dummyEventData = mutableListOf<Event>()
    
    init {
        dummyEventData = DummyData.allEvents.toMutableList()
        subject.onNext(dummyEventData)
        
        dummyEventData.add(
            Event(
                id = dummyEventData.size + 1,
                type = EventType.MEAL,
                desc = "Nasi goreng",
                date = "2000-01-08",
                time = "07:00",
            )
        )
        subject.onNext(dummyEventData)
    }
    
    override fun downloadEventData(patientId: Int): Flowable<Result<Nothing>> {
        dummyEventData = DummyData.allEvents.toMutableList()
        subject.onNext(dummyEventData)
        return Flowable.just(Result.Success(null))
    }
    
    override fun getEventData(): Flowable<List<Event>> {
        return subject.toFlowable(BackpressureStrategy.BUFFER)
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
        subject.onNext(dummyEventData)
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
        subject.onNext(dummyEventData)
        return Flowable.just(Result.Success(null))
    }
    
    override fun deleteEvent(id: Int): Flowable<Result<Nothing>> {
        dummyEventData.removeIf { it.id == id }
        subject.onNext(dummyEventData)
        return Flowable.just(Result.Success(null))
    }
    
    override fun clearEventData(): Completable {
        dummyEventData.clear()
        subject.onNext(dummyEventData)
        return Completable.complete()
    }
    
}