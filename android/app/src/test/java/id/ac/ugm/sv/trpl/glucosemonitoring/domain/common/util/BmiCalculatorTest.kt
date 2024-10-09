package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import junit.framework.TestCase.assertEquals
import org.junit.Test

class BmiCalculatorTest {
    
    private val dummyWeight = 60
    private val dummyHeight = 170
    
    @Test
    fun `Calculate the BMI, given both weight and height are non-zero, return a non-zero result`() {
        val expectedBmi = 20.8f
        val actualBmi = calculateBmi(dummyWeight, dummyHeight)
        
        assertEquals(expectedBmi, actualBmi)
    }
    
    @Test
    fun `Calculate the BMI, given a zero weight, return a zero result`() {
        val expectedBmi = 0.0f
        val actualBmi = calculateBmi(0, dummyHeight)
        
        assertEquals(expectedBmi, actualBmi)
    }
    
    @Test
    fun `Calculate the BMI, given a zero height, return a zero result`() {
        val expectedBmi = 0.0f
        val actualBmi = calculateBmi(dummyWeight, 0)
        
        assertEquals(expectedBmi, actualBmi)
    }
    
    @Test
    fun `Calculate the BMI, given both weight and height are zero, return a zero result`() {
        val expectedBmi = 0.0f
        val actualBmi = calculateBmi(0, 0)
        
        assertEquals(expectedBmi, actualBmi)
    }
    
    @Test
    fun `Calculate healthy weight range, given a non-zero height, return a range of non-zero values`() {
        val expectedHealthyWeightRange = 53..72
        val actualHealthyWeightRange = calculateHealthyWeight(dummyHeight)
        
        assertEquals(expectedHealthyWeightRange, actualHealthyWeightRange)
    }
    
    @Test
    fun `Calculate healthy weight range, given a zero height, return a range of zero values`() {
        val expectedHealthyWeightRange = 0..0
        val actualHealthyWeightRange = calculateHealthyWeight(0)
        
        assertEquals(expectedHealthyWeightRange, actualHealthyWeightRange)
    }
    
}