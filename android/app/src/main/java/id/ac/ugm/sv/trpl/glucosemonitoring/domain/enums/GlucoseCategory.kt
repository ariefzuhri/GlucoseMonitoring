package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

// Source: American Diabetes Association (ADA)
enum class GlucoseCategory(val range: IntRange) {
    HYPERGLYCEMIA_LEVEL_2(251..Int.MAX_VALUE),
    HYPERGLYCEMIA_LEVEL_1(181..250),
    IN_RANGE(70..180),
    HYPOGLYCEMIA_LEVEL_1(54..69),
    HYPOGLYCEMIA_LEVEL_2(0..53),
    UNKNOWN(IntRange.EMPTY),
    ;
    
    companion object {
        fun fromInt(value: Int?) =
            values().find { it.range.contains(value) } ?: UNKNOWN
    }
    
}