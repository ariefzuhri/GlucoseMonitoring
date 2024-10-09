package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

enum class DateFormat(val pattern: String) {
    RAW_DATE("yyyy-MM-dd"),
    RAW_TIME("HH:mm"),
    RAW_FULL("yyyy-MM-dd HH:mm"),
    
    READABLE_LONG_DATE("d MMM yyyy"),
    READABLE_SHORT_DATE("d MMM"),
    READABLE_TIME("HH.mm"),
    
    CHART_FULL("HH.mm, d MMM yyyy"),
    CHART_LONG("dd/MM HH.mm"),
    CHART_SHORT("HH.mm"),
}