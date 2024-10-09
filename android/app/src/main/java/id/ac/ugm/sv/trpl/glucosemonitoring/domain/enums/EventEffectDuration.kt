package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

// Source: American Diabetes Association (ADA)
enum class EventEffectDuration(val maxHours: Long) {
    MEAL(2),
    EXERCISE(24),
    MEDICATION(24),
}