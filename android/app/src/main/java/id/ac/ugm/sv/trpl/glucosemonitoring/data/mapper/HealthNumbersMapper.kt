package id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.HealthNumbers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthNumbersMapper @Inject constructor() {
    
    fun mapToDomain(
        weight: String,
        height: String,
        systolic: String,
        diastolic: String,
    ): HealthNumbers {
        return HealthNumbers(
            weight = weight.toIntOrNull(),
            height = height.toIntOrNull(),
            systolic = systolic.toIntOrNull(),
            diastolic = diastolic.toIntOrNull(),
        )
    }
    
}