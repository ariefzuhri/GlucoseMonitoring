package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.custom.chart.valueformatter

import com.github.mikephil.charting.formatter.ValueFormatter

class PercentFormatter : ValueFormatter() {
    
    override fun getFormattedValue(value: Float): String {
        return "%.0f%%".format(value)
    }
    
}