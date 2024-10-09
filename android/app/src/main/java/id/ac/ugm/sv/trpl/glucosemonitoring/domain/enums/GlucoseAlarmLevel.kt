package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

enum class GlucoseAlarmLevel(val glucoseRange: IntRange) {
    DANGEROUSLY_HIGH(251..Int.MAX_VALUE),
    HIGH(181..250),
    TOWARDS_HIGH(171..180),
    NORMAL(75..170),
    TOWARDS_LOW(70..74),
    LOW(54..69),
    DANGEROUSLY_LOW(0..53),
    ;
    
    companion object {
        fun fromInt(value: Int?) =
            values().find { it.glucoseRange.contains(value) }!!
    }
    
}