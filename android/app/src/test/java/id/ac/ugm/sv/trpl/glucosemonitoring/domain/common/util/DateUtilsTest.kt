package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DateUtilsTest {
    
    /* @Test
    fun `Subtract hours from a given date, return the date after subtraction`() {
        val date = Calendar.getInstance().time
        val hoursToSubtract = 1
        
        val expectedDate = Calendar.getInstance().apply {
            time = date
            add(Calendar.HOUR_OF_DAY, -hoursToSubtract)
        }.time
        val actualDate = date.minusHours(hoursToSubtract)
        
        assertEquals(expectedDate, actualDate)
    } */
    
    /* @Test
    fun `Add hours from a given date, return the date after addition`() {
        val date = Calendar.getInstance().time
        val hoursToAdd = 1
        
        val expectedDate = Calendar.getInstance().apply {
            time = date
            add(Calendar.HOUR_OF_DAY, hoursToAdd)
        }.time
        val actualDate = date.plusHours(hoursToAdd)
        
        assertEquals(expectedDate, actualDate)
    } */
    
    @Test
    fun `Convert a date string to a Date object, given a date string with its format, return the Date object representation`() {
        val dateString = "2023-05-06"
        val inputFormat = DateFormat.RAW_DATE
        
        val expectedDate = SimpleDateFormat(inputFormat.pattern, LOCALE_INDONESIAN)
            .parse(dateString) as Date
        val actualDate = dateString.toDate(inputFormat)
        
        assertEquals(expectedDate, actualDate)
    }
    
    /* @Test
    fun `Convert a Date object to a date string, given a Date object with the output format, return the date string`() {
        val date = Calendar.getInstance().time
        val outputFormat = DateFormat.RAW_DATE
        
        val expectedDateString = SimpleDateFormat(outputFormat.pattern, LOCALE_INDONESIAN)
            .format(date)
        val actualDateString = date.toString(outputFormat)
        
        assertEquals(expectedDateString, actualDateString)
    } */
    
    /* @Test
    fun `Convert the milliseconds to a Date object, given a date in milliseconds, return the Date object representation`() {
        val dateInMillis = Calendar.getInstance().timeInMillis
        
        val expectedDate = Calendar.getInstance().apply {
            timeInMillis = dateInMillis
        }.time
        val actualDate = dateInMillis.toDate()
        
        assertEquals(expectedDate, actualDate)
    } */
    
    @Test
    fun `Get the current date, return today date`() {
        val expectedDate = Calendar.getInstance().time
        val actualDate = getCurrentDateTime()
        
        assertEquals(expectedDate, actualDate)
    }
    
}