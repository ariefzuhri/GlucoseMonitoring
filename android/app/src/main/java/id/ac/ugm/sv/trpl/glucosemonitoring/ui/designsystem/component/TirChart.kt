package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.custom.chart.valueformatter.PercentFormatter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.alpha
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toTypeface
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.green
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.orange
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.red

@Composable
fun TirChart(
    timeInRange: Int,
    timeAboveRange: Int,
    timeBelowRange: Int,
    modifier: Modifier = Modifier,
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val greenColor = MaterialTheme.colorScheme.green.toArgb()
    val orangeColor = MaterialTheme.colorScheme.orange.toArgb()
    val redColor = MaterialTheme.colorScheme.red.toArgb()
    
    val labelLargeType = MaterialTheme.typography.labelLarge.toTypeface()
    val labelMediumType = MaterialTheme.typography.labelMedium.toTypeface()
    
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier,
    ) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        600,
                    )
                    initChartView(
                        onSurfaceColor = onSurfaceColor,
                        labelMediumType = labelMediumType,
                    )
                }
            },
            update = { view ->
                view.apply {
                    setChartData(
                        timeInRange = timeInRange,
                        timeAboveRange = timeAboveRange,
                        timeBelowRange = timeBelowRange,
                        onSurfaceColor = onSurfaceColor,
                        greenColor = greenColor,
                        orangeColor = orangeColor,
                        redColor = redColor,
                        labelMediumType = labelLargeType,
                    )
                }
            },
            modifier = Modifier
                .padding(24.dp),
        )
    }
}

private fun PieChart.initChartView(
    onSurfaceColor: Int,
    labelMediumType: Typeface,
) {
    description.isEnabled = false
    isDrawHoleEnabled = false
    isHighlightPerTapEnabled = true
    isNestedScrollingEnabled = true
    setDrawCenterText(false)
    setDrawEntryLabels(false)
    setTouchEnabled(true)
    
    rotationAngle = 45f
    isRotationEnabled = true
    
    // Display legend and set its style
    legend.apply {
        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        orientation = Legend.LegendOrientation.HORIZONTAL
        setDrawInside(false)
        
        form = Legend.LegendForm.CIRCLE
        formSize = 8f
        
        textColor = onSurfaceColor
        textSize = 10f
        typeface = labelMediumType
        isWordWrapEnabled = true
    }
}

private fun PieChart.setChartData(
    timeInRange: Int,
    timeAboveRange: Int,
    timeBelowRange: Int,
    onSurfaceColor: Int,
    greenColor: Int,
    orangeColor: Int,
    redColor: Int,
    labelMediumType: Typeface,
) {
    // Create data set for TIR chart
    val dataEntries = listOf(
        PieEntry(
            timeInRange.toFloat(),
            context.getString(R.string.txt_time_in_range_tirchart),
        ),
        PieEntry(
            timeAboveRange.toFloat(),
            context.getString(R.string.txt_time_above_range_tirchart),
        ),
        PieEntry(
            timeBelowRange.toFloat(),
            context.getString(R.string.txt_time_below_range_tirchart),
        ),
    )
    val dataset = PieDataSet(dataEntries, null).apply {
        setDrawIcons(false)
        sliceSpace = 3f
        selectionShift = 5f
        
        val sliceColors = listOf(
            greenColor.alpha(0.8f),
            redColor.alpha(0.8f),
            orangeColor.alpha(0.8f),
        )
        colors = sliceColors
        
        setValueTextColors(sliceColors)
        
        yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        valueLineColor = onSurfaceColor.alpha(0.4f)
        valueLineWidth = 2f
        valueLinePart1Length = 0.4f
        valueLinePart2Length = 0.9f
    }
    
    val chartData = PieData(dataset).apply {
        // Set entry text style
        setValueFormatter(PercentFormatter())
        setValueTextSize(14f)
        setValueTypeface(labelMediumType)
    }
    data = chartData
    
    invalidate()
    
    // Animate displaying data
    animateY(1200)
}

@Preview(showBackground = true)
@Composable
private fun TirChartPreview() {
    GlucoverTheme {
        TirChart(
            timeInRange = 220,
            timeAboveRange = 60,
            timeBelowRange = 8,
        )
    }
}