package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import junit.framework.TestCase.assertEquals
import org.junit.Test

class MathTest {
    
    @Test
    fun `Calculate the average of a list of integers, given a list of integers, return the correct result`() {
        val dummyList = listOf(1, 2, 3, 4, 5)
        
        val expectedAverage = 3.0f
        val actualAverage = dummyList.averageOfOrNull { it }
        
        assertEquals(expectedAverage, actualAverage)
    }
    
    @Test
    fun `Calculate the average of a list of integers, given an empty list of integers, return null`() {
        val dummyList = listOf<Int>()
        
        val expectedAverage: Float? = null
        val actualAverage = dummyList.averageOfOrNull { it }
        
        assertEquals(expectedAverage, actualAverage)
    }
    
    @Test
    @Suppress("RedundantNullableReturnType")
    fun `Subtract the second integer from the first integer, given two integers, return the difference`() {
        val dummyFirst: Int? = 5
        val dummySecond = 3
        
        val expectedDifference = 2
        val actualDifference = dummyFirst.minus(dummySecond)
        
        assertEquals(expectedDifference, actualDifference)
    }
    
    @Test
    fun `Subtract the second integer from the first integer, given both integers as null, return null`() {
        val dummyFirst = null
        val dummySecond = null
        
        val expectedDifference: Int? = null
        val actualDifference = dummyFirst.minus(dummySecond)
        
        assertEquals(expectedDifference, actualDifference)
    }
    
    @Test
    fun `Subtract the second integer from the first integer, given the second integer as null, return null`() {
        val dummyFirst = 5
        val dummySecond = null
        
        val expectedDifference: Int? = null
        val actualDifference = dummyFirst.minus(dummySecond)
        
        assertEquals(expectedDifference, actualDifference)
    }
    
    @Test
    fun `Subtract the second integer from the first integer, given the first integer as null, return null`() {
        val dummyFirst = null
        val dummySecond = 3
        
        val expectedDifference: Int? = null
        val actualDifference = dummyFirst.minus(dummySecond)
        
        assertEquals(expectedDifference, actualDifference)
    }
    
    @Test
    fun `Convert a value in centimeters to meters, return the correct result`() {
        val dummyCM = 170
        
        val expectedMeters = 1.7f
        val actualMeters = cmToMeters(dummyCM)
        
        assertEquals(expectedMeters, actualMeters)
    }
    
    @Test
    fun `Calculate the percentage of the value, given both value and total are non-zero, return the correct percentage rounded down`() {
        val dummyValue = 25
        val dummyTotal = 30
        
        val expectedPercentage = 83
        val actualPercentage = percentageOf(dummyValue, dummyTotal)
        
        assertEquals(expectedPercentage, actualPercentage)
    }
    
    @Test
    fun `Calculate the percentage of the value, given both value and total are non-zero, return the correct percentage rounded up`() {
        val dummyValue = 26
        val dummyTotal = 30
        
        val expectedPercentage = 87
        val actualPercentage = percentageOf(dummyValue, dummyTotal)
        
        assertEquals(expectedPercentage, actualPercentage)
    }
    
    @Test
    fun `Calculate the percentage of the value, given the total is zero, return null percentage`() {
        val dummyValue = 25
        val dummyTotal = 0
        
        val expectedPercentage: Int? = null
        val actualPercentage = percentageOf(dummyValue, dummyTotal)
        
        assertEquals(expectedPercentage, actualPercentage)
    }
    
}