package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.BloodPressureCategory
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BloodPressureClassifierTest {
    
    private val normalSystolic = 115
    private val elevatedSystolic = 120
    private val hypertensionStage1Systolic = 130
    private val hypertensionStage2Systolic = 140
    private val hypertensiveCrisisSystolic = 185
    
    private val normalDiastolic = 75
    private val hypertensionStage1Diastolic = 80
    private val hypertensionStage2Diastolic = 90
    private val hypertensiveCrisisDiastolic = 125
    
    @Test
    fun `Classify normal systolic and normal diastolic, return normal blood pressure`() {
        val expectedCategory = BloodPressureCategory.NORMAL
        val actualCategory = classifyBloodPressure(
            normalSystolic, normalDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify normal systolic and hypertension stage 1 diastolic, return hypertension stage 1`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_1
        val actualCategory = classifyBloodPressure(
            normalSystolic, hypertensionStage1Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify normal systolic and hypertension stage 2 diastolic, return hypertension stage 2`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_2
        val actualCategory = classifyBloodPressure(
            normalSystolic, hypertensionStage2Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify normal systolic and hypertensive crisis diastolic, return hypertensive crisis diastolic`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            normalSystolic, hypertensiveCrisisDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify elevated systolic and normal diastolic, return elevated blood pressure`() {
        val expectedCategory = BloodPressureCategory.ELEVATED
        val actualCategory = classifyBloodPressure(
            elevatedSystolic, normalDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify elevated systolic and hypertension stage 1 diastolic, return hypertension stage 1`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_1
        val actualCategory = classifyBloodPressure(
            elevatedSystolic, hypertensionStage1Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify elevated systolic and hypertension stage 2 diastolic, return hypertension stage 2`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_2
        val actualCategory = classifyBloodPressure(
            elevatedSystolic, hypertensionStage2Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify elevated systolic and hypertensive crisis diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            elevatedSystolic, hypertensiveCrisisDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 1 systolic and normal diastolic, return hypertension stage 1`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_1
        val actualCategory = classifyBloodPressure(
            hypertensionStage1Systolic, normalDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 1 systolic and hypertension stage 1 diastolic, return hypertension stage 1`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_1
        val actualCategory = classifyBloodPressure(
            hypertensionStage1Systolic, hypertensionStage1Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 1 systolic and hypertension stage 2 diastolic, return hypertension stage 2`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_2
        val actualCategory = classifyBloodPressure(
            hypertensionStage1Systolic, hypertensionStage2Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 1 systolic and hypertensive crisis diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            hypertensionStage1Systolic, hypertensiveCrisisDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 2 systolic and normal diastolic, return hypertension stage 2`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_2
        val actualCategory = classifyBloodPressure(
            hypertensionStage2Systolic, normalDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 2 systolic and hypertension stage 1 diastolic, return hypertension stage 2`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_2
        val actualCategory = classifyBloodPressure(
            hypertensionStage2Systolic, hypertensionStage1Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 2 systolic and hypertension stage 2 diastolic, return hypertension stage 2`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSION_STAGE_2
        val actualCategory = classifyBloodPressure(
            hypertensionStage2Systolic, hypertensionStage2Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertension stage 2 systolic and hypertensive crisis diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            hypertensionStage2Systolic, hypertensiveCrisisDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertensive crisis systolic and normal diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            hypertensiveCrisisSystolic, normalDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertensive crisis systolic and hypertension stage 1 diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            hypertensiveCrisisSystolic, hypertensionStage1Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertensive crisis systolic and hypertension stage 2 diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            hypertensiveCrisisSystolic, hypertensionStage2Diastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
    @Test
    fun `Classify hypertensive crisis systolic and hypertensive crisis diastolic, return hypertensive crisis`() {
        val expectedCategory = BloodPressureCategory.HYPERTENSIVE_CRISIS
        val actualCategory = classifyBloodPressure(
            hypertensiveCrisisSystolic, hypertensiveCrisisDiastolic
        )
        
        assertEquals(expectedCategory, actualCategory)
    }
    
}