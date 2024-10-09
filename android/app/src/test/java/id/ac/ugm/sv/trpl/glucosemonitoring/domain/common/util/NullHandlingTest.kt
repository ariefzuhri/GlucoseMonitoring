package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import junit.framework.TestCase.assertEquals
import org.junit.Test

class NullHandlingTest {
    
    @Test
    fun `Get invalid float, return -1f as the invalid float`() {
        val expectedInvalidFloat = -1f
        val actualInvalidFloat = Float.INVALID
        
        assertEquals(expectedInvalidFloat, actualInvalidFloat)
    }
    
    @Test
    fun `Get zero float, return 0f as the zero float`() {
        val expectedZeroInt = 0f
        val actualZeroInt = Float.ZERO
        
        assertEquals(expectedZeroInt, actualZeroInt)
    }
    
    @Test
    fun `Get invalid integer, return -1 as the invalid integer`() {
        val expectedInvalidInt = -1
        val actualInvalidInt = Int.INVALID
        
        assertEquals(expectedInvalidInt, actualInvalidInt)
    }
    
    @Test
    fun `Get zero integer, return 0 as the zero integer`() {
        val expectedZeroInt = 0
        val actualZeroInt = Int.ZERO
        
        assertEquals(expectedZeroInt, actualZeroInt)
    }
    
    @Test
    fun `Get empty string, return an empty string with zero characters`() {
        val expectedEmptyString = ""
        val actualEmptyString = String.EMPTY
        
        assertEquals(expectedEmptyString, actualEmptyString)
    }
    
    @Test
    fun `Get none string, return a em-dash as the none string`() {
        val expectedNoneString = "—"
        val actualNoneString = String.NONE
        
        assertEquals(expectedNoneString, actualNoneString)
    }
    
    @Test
    fun `Invoke or invalid, given a non-null float, return the float itself`() {
        val dummyFloat = 0f
        
        val expectedOrInvalid = 0f
        val actualOrInvalid = dummyFloat.orInvalid()
        
        assertEquals(expectedOrInvalid, actualOrInvalid)
    }
    
    @Test
    fun `Invoke or invalid, given a null float, return the invalid float`() {
        val dummyFloat: Float? = null
        
        val expectedOrInvalid = -1f
        val actualOrInvalid = dummyFloat.orInvalid()
        
        assertEquals(expectedOrInvalid, actualOrInvalid)
    }
    
    @Test
    fun `Invoke or invalid, given a non-null integer, return the integer itself`() {
        val dummyInt = 0
        
        val expectedOrInvalid = 0
        val actualOrInvalid = dummyInt.orInvalid()
        
        assertEquals(expectedOrInvalid, actualOrInvalid)
    }
    
    @Test
    fun `Invoke or invalid, given a null integer, return the invalid integer`() {
        val dummyInt: Int? = null
        
        val expectedOrInvalid = -1
        val actualOrInvalid = dummyInt.orInvalid()
        
        assertEquals(expectedOrInvalid, actualOrInvalid)
    }
    
    @Test
    fun `Invoke or zero, given a non-null integer, return the integer itself`() {
        val dummyInt = 0
        
        val expectedOrZero = 0
        val actualOrZero = dummyInt.orZero()
        
        assertEquals(expectedOrZero, actualOrZero)
    }
    
    @Test
    fun `Invoke or zero, given a null integer, return the zero integer`() {
        val dummyInt: Int? = null
        
        val expectedOrZero = 0
        val actualOrZero = dummyInt.orZero()
        
        assertEquals(expectedOrZero, actualOrZero)
    }
    
    @Test
    fun `Convert an integer to a string or empty, given a non-null integer, return the string representation`() {
        val dummyInt = 0
        
        val expectedToStringOrEmpty = "0"
        val actualToStringOrEmpty = dummyInt.toStringOrEmpty()
        
        assertEquals(expectedToStringOrEmpty, actualToStringOrEmpty)
    }
    
    @Test
    fun `Convert an integer to a string or empty, given a null integer, return the empty string`() {
        val dummyInt: Int? = null
        
        val expectedToStringOrEmpty = ""
        val actualToStringOrEmpty = dummyInt.toStringOrEmpty()
        
        assertEquals(expectedToStringOrEmpty, actualToStringOrEmpty)
    }
    
    @Test
    fun `Convert an integer to a string or none, given a non-null integer, return the string representation`() {
        val dummyInt = 0
        
        val expectedOrNone = "0"
        val actualOrNone = dummyInt.toStringOrNone()
        
        assertEquals(expectedOrNone, actualOrNone)
    }
    
    @Test
    fun `Convert an integer to a string or none, given a null integer, return the none string`() {
        val dummyInt: Int? = null
        
        val expectedOrNone = "—"
        val actualOrNone = dummyInt.toStringOrNone()
        
        assertEquals(expectedOrNone, actualOrNone)
    }
    
    @Test
    fun `Invoke or none, given a non-null string, return the string itself`() {
        val dummyString = "string"
        
        val expectedOrNone = "string"
        val actualOrNone = dummyString.orNone()
        
        assertEquals(expectedOrNone, actualOrNone)
    }
    
    @Test
    fun `Invoke or none, given a null string, return the none string`() {
        val dummyString: String? = null
        
        val expectedOrNone = "—"
        val actualOrNone = dummyString.orNone()
        
        assertEquals(expectedOrNone, actualOrNone)
    }
    
}