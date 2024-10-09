package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.BloodPressureCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.BloodPressureCategory.DiastolicRanges
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.BloodPressureCategory.SystolicRanges

// Source: American Heart Association (AHA), American Diabetes Association (ADA)
fun classifyBloodPressure(systolic: Int, diastolic: Int): BloodPressureCategory {
    val systolicRange = SystolicRanges.values()
        .first { it.range.contains(systolic) }
    val diastolicRange = DiastolicRanges.values()
        .first { it.range.contains(diastolic) }
    
    return when {
        systolicRange == SystolicRanges.HYPERTENSIVE_CRISIS ||
                diastolicRange == DiastolicRanges.HYPERTENSIVE_CRISIS
        -> BloodPressureCategory.HYPERTENSIVE_CRISIS
        
        systolicRange == SystolicRanges.HYPERTENSION_STAGE_2 ||
                diastolicRange == DiastolicRanges.HYPERTENSION_STAGE_2
        -> BloodPressureCategory.HYPERTENSION_STAGE_2
        
        systolicRange == SystolicRanges.HYPERTENSION_STAGE_1 ||
                diastolicRange == DiastolicRanges.HYPERTENSION_STAGE_1
        -> BloodPressureCategory.HYPERTENSION_STAGE_1
        
        systolicRange == SystolicRanges.ELEVATED &&
                (diastolicRange == DiastolicRanges.NORMAL ||
                        diastolicRange == DiastolicRanges.ELEVATED)
        -> BloodPressureCategory.ELEVATED
        
        systolicRange == SystolicRanges.NORMAL &&
                diastolicRange == DiastolicRanges.NORMAL
        -> BloodPressureCategory.NORMAL
        
        else -> BloodPressureCategory.HYPERTENSION_STAGE_2
    }
}