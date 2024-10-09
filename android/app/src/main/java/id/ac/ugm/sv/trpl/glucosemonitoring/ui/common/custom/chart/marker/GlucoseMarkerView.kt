package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.custom.chart.marker

import android.annotation.SuppressLint
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.databinding.GlucoseMarkerViewBinding
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.timeMillisToDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.shouldGone
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class GlucoseMarkerView(
    chart: Chart<*>,
    layoutResource: Int,
) : MarkerView(chart.context, layoutResource) {
    
    private val binding = GlucoseMarkerViewBinding.bind(this)
    
    private val totalWidth = chart.width
    
    init {
        // Set the marker to the chart
        chartView = chart
        chart.marker = this
    }
    
    override fun refreshContent(entry: Entry, highlight: Highlight) {
        binding.apply {
            val timeInMillis = entry.x.toLong()
            val glucoseLevel = entry.y.roundToInt()
            val glucoseCategory = GlucoseCategory.fromInt(glucoseLevel)
            
            txtDate.text = timeMillisToDateTime(timeInMillis).toString(DateFormat.CHART_FULL)
            @SuppressLint("SetTextI18n")
            txtGlucoseLevel.text = "$glucoseLevel ${context.getString(R.string.glucose_unit)}"
            txtGlucoseCategory.text = context.getString(
                when (glucoseCategory) {
                    GlucoseCategory.HYPERGLYCEMIA_LEVEL_2 -> R.string.glucose_category_hyperglycemia_level_2
                    GlucoseCategory.HYPERGLYCEMIA_LEVEL_1 -> R.string.glucose_category_hyperglycemia_level_1
                    GlucoseCategory.IN_RANGE -> R.string.glucose_category_in_range
                    GlucoseCategory.HYPOGLYCEMIA_LEVEL_1 -> R.string.glucose_category_hypoglycemia_level_2
                    GlucoseCategory.HYPOGLYCEMIA_LEVEL_2 -> R.string.glucose_category_hypoglycemia_level_1
                    GlucoseCategory.UNKNOWN -> R.string.glucose_category_unknown
                }
            )
            
            txtGlucoseLevel.shouldGone(glucoseCategory == GlucoseCategory.UNKNOWN)
            
            val data = entry.data
            if (data is Event) {
                txtGlucoseCategory.text = data.desc
            }
        }
        
        super.refreshContent(entry, highlight)
    }
    
    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val supposedX = posX + width
        val mpPointF = MPPointF()
        
        mpPointF.x = when {
            posX - width < 0 -> 0f
            supposedX > totalWidth -> -width.toFloat()
            else -> 0f
        }
        
        mpPointF.y = if (posY > height)
            -height.toFloat()
        else
            0f
        
        return mpPointF
    }
    
}