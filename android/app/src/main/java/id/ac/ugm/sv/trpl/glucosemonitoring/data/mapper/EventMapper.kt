package id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.EventEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.DeleteEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetEventsResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PostEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.PutEventResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.Default
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventMapper @Inject constructor() {
    
    fun mapToDomain(input: List<EventEntity>): List<Event> {
        return input.map { entity ->
            mapToDomain(entity)
        }
    }
    
    private fun mapToDomain(input: EventEntity): Event {
        return Event(
            id = input.id,
            type = EventType.of(input.type),
            desc = input.desc,
            date = input.date,
            time = input.time,
        )
    }
    
    fun mapToEntities(input: GetEventsResponse): List<EventEntity> {
        return input.data?.mapNotNull { dataItem ->
            mapToEntity(dataItem!!)
        } ?: emptyList()
    }
    
    private fun mapToEntity(input: GetEventsResponse.DataItem): EventEntity {
        return EventEntity(
            id = input.evId ?: Default.id,
            type = input.evType ?: Default.string,
            desc = input.evDesc ?: Default.string,
            date = input.evDate ?: Default.string,
            time = input.evTime ?: Default.string,
        )
    }
    
    fun mapToEntity(input: PostEventResponse.Data): EventEntity {
        return EventEntity(
            id = input.evId ?: Default.id,
            type = input.evType ?: Default.string,
            desc = input.evDesc ?: Default.string,
            date = input.evDate ?: Default.string,
            time = input.evTime ?: Default.string,
        )
    }
    
    fun mapToEntity(input: PutEventResponse.Data): EventEntity {
        return EventEntity(
            id = input.evId ?: Default.id,
            type = input.evType ?: Default.string,
            desc = input.evDesc ?: Default.string,
            date = input.evDate ?: Default.string,
            time = input.evTime ?: Default.string,
        )
    }
    
    fun mapToEntity(input: DeleteEventResponse.Data): EventEntity {
        return EventEntity(
            id = input.evId ?: Default.id,
            type = input.evType ?: Default.string,
            desc = input.evDesc ?: Default.string,
            date = input.evDate ?: Default.string,
            time = input.evTime ?: Default.string,
        )
    }
    
}