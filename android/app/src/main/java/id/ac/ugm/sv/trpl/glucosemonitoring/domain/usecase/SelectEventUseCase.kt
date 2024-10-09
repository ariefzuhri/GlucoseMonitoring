package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SelectEventUseCase @Inject constructor(
    private val eventRepository: IEventRepository,
) {
    
    operator fun invoke(id: Int): Flowable<Event> {
        return eventRepository.getEventData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { eventData -> eventData.filter { it.id == id } }
            .takeWhile { it.isNotEmpty() }
            .map { it.first() }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}