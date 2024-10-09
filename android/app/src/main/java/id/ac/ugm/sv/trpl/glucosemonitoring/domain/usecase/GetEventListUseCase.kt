package id.ac.ugm.sv.trpl.glucosemonitoring.domain.usecase

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IEventRepository
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.repository.IGlucoseRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetEventListUseCase @Inject constructor(
    private val eventRepository: IEventRepository,
    private val glucoseRepository: IGlucoseRepository,
) {
    
    operator fun invoke(): Flowable<List<Event>> {
        return glucoseRepository.getGlucoseData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap { glucoseData ->
                eventRepository.getEventData().map { eventData ->
                    eventData.forEach { event ->
                        if (event.type == EventType.MEAL) {
                            event.populateRelatedGlucoseData(
                                allGlucoseData = glucoseData
                            )
                        }
                    }
                    eventData.sortedBy { it.dateTime }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
    
}