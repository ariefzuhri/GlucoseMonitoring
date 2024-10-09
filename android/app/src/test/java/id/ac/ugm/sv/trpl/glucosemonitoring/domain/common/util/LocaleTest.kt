package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

class LocaleTest {
    
    @Test
    fun `Get locale for Indonesian, return correct locale`() {
        val expectedLocale = Locale("in", "ID")
        val actualLocale = LOCALE_INDONESIAN
        
        assertEquals(expectedLocale, actualLocale)
    }
    
}