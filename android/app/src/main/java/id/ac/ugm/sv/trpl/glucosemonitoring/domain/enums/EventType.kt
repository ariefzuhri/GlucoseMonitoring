package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

enum class EventType(val id: String) {
    MEAL("meal"),
    EXERCISE("exercise"),
    MEDICATION("medication"),
    ;
    
    companion object {
        fun of(id: String) = values().find { it.id == id }
            ?: MEAL
    }
    
}