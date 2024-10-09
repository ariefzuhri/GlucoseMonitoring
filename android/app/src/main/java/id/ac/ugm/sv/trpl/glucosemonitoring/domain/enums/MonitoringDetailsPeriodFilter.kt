package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

enum class MonitoringDetailsPeriodFilter {
    LAST_24_HOUR,
    SEVEN_DAYS,
    FOURTEEN_DAYS,
    THIRTY_DAYS,
    NINETY_DAYS,
    CUSTOM,
    ;
    
    companion object {
        fun of(name: String) =
            values().find { it.name == name } ?: LAST_24_HOUR
    }
    
}