package id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper

import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity.GlucoseEntity
import id.ac.ugm.sv.trpl.glucosemonitoring.data.source.remote.response.GetGlucosesResponse
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.Default
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlucoseMapper @Inject constructor() {
    
    fun mapToDomain(input: List<GlucoseEntity>): List<Glucose> {
        return input.map { entity ->
            mapToDomain(entity)
        }
    }
    
    private fun mapToDomain(input: GlucoseEntity): Glucose {
        return Glucose(
            id = input.id,
            level = input.level,
            date = input.date,
            time = input.time,
        )
    }
    
    fun mapToDomain(input: GetGlucosesResponse): List<Glucose> {
        return input.data?.mapNotNull { dataItem ->
            mapToDomain(dataItem!!)
        } ?: emptyList()
    }
    
    private fun mapToDomain(input: GetGlucosesResponse.DataItem): Glucose {
        return Glucose(
            id = input.recId ?: Default.id,
            level = input.bgLevel ?: Default.float,
            date = input.bgDate ?: Default.string,
            time = input.bgTime ?: Default.string,
        )
    }
    
    fun mapToEntities(input: GetGlucosesResponse): List<GlucoseEntity> {
        return input.data?.mapNotNull { dataItem ->
            mapToEntity(dataItem!!)
        } ?: emptyList()
    }
    
    private fun mapToEntity(input: GetGlucosesResponse.DataItem): GlucoseEntity {
        return GlucoseEntity(
            id = input.recId ?: Default.id,
            level = input.bgLevel ?: Default.float,
            date = input.bgDate ?: Default.string,
            time = input.bgTime ?: Default.string,
        )
    }
    
}