package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.custom.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.timeMillisToDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat

class DateTimeFormatter : ValueFormatter() {
    
    var dateFormat = DateFormat.CHART_SHORT
    
    override fun getFormattedValue(value: Float): String {
        return timeMillisToDateTime(value.toLong()).toString(dateFormat)
    }
    
}