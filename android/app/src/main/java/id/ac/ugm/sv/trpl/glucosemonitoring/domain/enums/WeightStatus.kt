package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

// Source: World Health Organization (WHO), American Diabetes Association (ADA)
enum class WeightStatus(val range: ClosedFloatingPointRange<Float>) {
    UNDERWEIGHT(0.0f..18.4f),
    NORMAL_WEIGHT(18.5f..24.9f),
    OVERWEIGHT(25.0f..29.9f),
    OBESITY_CLASS_1(30.0f..34.9f),
    OBESITY_CLASS_2(35.0f..39.9f),
    OBESITY_CLASS_3(40.0f..Float.MAX_VALUE),
    ;
    
    companion object {
        fun fromBmi(bmi: Float) =
            values().first { it.range.contains(bmi) }
    }
    
}