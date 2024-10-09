package id.ac.ugm.sv.trpl.glucosemonitoring.data.mapper

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.LastSeen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastSeenMapper @Inject constructor() {
    
    fun mapToDomain(
        glucoseLevel: String,
        glucoseTime: String,
    ): LastSeen {
        return LastSeen(
            glucoseLevel = glucoseLevel.toIntOrNull(),
            glucoseTime = glucoseTime.ifEmpty { null },
        )
    }
    
}