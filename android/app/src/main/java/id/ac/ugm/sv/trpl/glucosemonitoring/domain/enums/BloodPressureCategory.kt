package id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums

// Source: American Heart Association (AHA), American Diabetes Association (ADA)
enum class BloodPressureCategory {
    NORMAL,
    ELEVATED,
    HYPERTENSION_STAGE_1,
    HYPERTENSION_STAGE_2,
    HYPERTENSIVE_CRISIS,
    ;
    
    enum class SystolicRanges(val range: IntRange) {
        NORMAL(0..119),
        ELEVATED(120..129),
        HYPERTENSION_STAGE_1(130..139),
        HYPERTENSION_STAGE_2(140..180),
        HYPERTENSIVE_CRISIS(181..Int.MAX_VALUE),
    }
    
    enum class DiastolicRanges(val range: IntRange) {
        NORMAL(0..79),
        ELEVATED(0..79),
        HYPERTENSION_STAGE_1(80..89),
        HYPERTENSION_STAGE_2(90..120),
        HYPERTENSIVE_CRISIS(121..Int.MAX_VALUE),
    }
    
}