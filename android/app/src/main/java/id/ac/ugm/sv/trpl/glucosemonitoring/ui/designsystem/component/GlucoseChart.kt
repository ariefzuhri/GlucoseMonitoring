package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.orInvalid
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.timeMillis
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.custom.chart.marker.GlucoseMarkerView
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.custom.chart.valueformatter.DateTimeFormatter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.alpha
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toTypeface
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.red
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.teal
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.violet
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.yellow

@Composable
fun GlucoseChart(
    glucoseData: List<Glucose>,
    modifier: Modifier = Modifier,
    title: String? = null,
    eventData: List<Event>? = null,
    isExpanded: Boolean = false,
    navigateToExpandedChart: () -> Unit = {},
) {
    val shouldShowExpandableButton = !isExpanded && glucoseData.isNotEmpty()
    
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val outlineVariantColor = MaterialTheme.colorScheme.outlineVariant.toArgb()
    val redColor = MaterialTheme.colorScheme.red.toArgb()
    val yellowColor = MaterialTheme.colorScheme.yellow.toArgb()
    val purpleColor = MaterialTheme.colorScheme.violet.toArgb()
    val tealColor = MaterialTheme.colorScheme.teal.toArgb()
    
    val bodyMediumType = MaterialTheme.typography.bodyMedium.toTypeface()
    val labelMediumType = MaterialTheme.typography.labelMedium.toTypeface()
    val labelSmallType = MaterialTheme.typography.labelSmall.toTypeface()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            AndroidView(
                factory = { context ->
                    LineChart(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else 600,
                        )
                        initChartView(
                            upperLimit = GlucoseCategory.IN_RANGE.range.last.toFloat(),
                            lowerLimit = GlucoseCategory.IN_RANGE.range.first.toFloat(),
                            isExpanded = isExpanded,
                            onSurfaceColor = onSurfaceColor,
                            outlineVariantColor = outlineVariantColor,
                            redColor = redColor,
                            bodyMediumType = bodyMediumType,
                            labelMediumType = labelMediumType,
                            labelSmallType = labelSmallType,
                        )
                    }
                },
                update = { view ->
                    view.setChartData(
                        glucoseData = glucoseData,
                        eventData = eventData,
                        upperLimit = GlucoseCategory.IN_RANGE.range.last.toFloat(),
                        lowerLimit = GlucoseCategory.IN_RANGE.range.first.toFloat(),
                        primaryColor = primaryColor,
                        yellowColor = yellowColor,
                        purpleColor = purpleColor,
                        tealColor = tealColor,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(
                        top = 24.dp,
                        bottom = if (!shouldShowExpandableButton) 24.dp else 0.dp,
                    ),
            )
            
            if (shouldShowExpandableButton) {
                ImageButton(
                    icon = GlucoverIcons.Expand,
                    contentDescription = R.string.btn_cd_expand_glucosechart,
                    onClick = navigateToExpandedChart,
                    modifier = Modifier
                        .padding(end = 24.dp, bottom = 24.dp)
                        .align(Alignment.End),
                )
            }
        }
    }
}

private fun LineChart.initChartView(
    upperLimit: Float,
    lowerLimit: Float,
    isExpanded: Boolean,
    bodyMediumType: Typeface,
    labelMediumType: Typeface,
    labelSmallType: Typeface,
    onSurfaceColor: Int,
    outlineVariantColor: Int,
    redColor: Int,
) {
    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry, h: Highlight) {
            setDrawMarkers(true)
        }
        
        override fun onNothingSelected() {}
    })
    
    description.isEnabled = false
    isDragEnabled = true
    isNestedScrollingEnabled = true
    setPinchZoom(true)
    setScaleEnabled(true)
    setTouchEnabled(true)
    
    // Create marker to display box when values are selected
    GlucoseMarkerView(this, R.layout.glucose_marker_view)
    
    // Set x-axis style
    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        axisLineColor = outlineVariantColor
        
        setDrawGridLines(false) // Disable vertical grid lines
        
        textColor = onSurfaceColor.alpha(0.5f)
        textSize = 12f
        typeface = labelSmallType
        
        valueFormatter = DateTimeFormatter().apply {
            if (isExpanded) dateFormat = DateFormat.CHART_LONG
        }
        labelRotationAngle = -45f
    }
    
    // Set y-axis style
    val yAxis = axisLeft.apply {
        axisLineColor = outlineVariantColor
        
        gridColor = onSurfaceColor.alpha(0.35f)
        enableGridDashedLine(10f, 10f, 0f)
        
        textColor = onSurfaceColor.alpha(0.5f)
        textSize = 12f
        typeface = labelSmallType
    }
    axisRight.isEnabled = false // Disable right y-axis
    
    // Draw a top limit line
    val upperLimitLine =
        LimitLine(upperLimit, context.getString(R.string.txt_upper_limit_glucosechart)).apply {
            lineColor = redColor.alpha(0.6f)
            lineWidth = 2f
            enableDashedLine(10f, 10f, 0f)
            
            labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            textSize = 9f
            textColor = onSurfaceColor.alpha(0.5f)
            typeface = bodyMediumType
        }
    yAxis.addLimitLine(upperLimitLine)
    
    // Draw a bottom limit line
    val lowerLimitLine =
        LimitLine(lowerLimit, context.getString(R.string.txt_lower_limit_glucosechart)).apply {
            lineColor = redColor.alpha(0.6f)
            lineWidth = 2f
            enableDashedLine(10f, 10f, 0f)
            
            labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            textSize = 9f
            textColor = onSurfaceColor.alpha(0.5f)
            typeface = bodyMediumType
        }
    yAxis.addLimitLine(lowerLimitLine)
    
    // Draw limit lines behind data
    xAxis.setDrawLimitLinesBehindData(true)
    yAxis.setDrawLimitLinesBehindData(true)
    
    // Display legend and set its style
    legend.apply {
        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        orientation = Legend.LegendOrientation.HORIZONTAL
        
        form = Legend.LegendForm.CIRCLE
        formSize = 8f
        
        textColor = onSurfaceColor
        textSize = 10f
        typeface = labelMediumType
        isWordWrapEnabled = true
    }
    extraBottomOffset = 16f // Add space between chart and legend
}

private fun LineChart.setChartData(
    glucoseData: List<Glucose>,
    eventData: List<Event>? = null,
    upperLimit: Float,
    lowerLimit: Float,
    primaryColor: Int,
    yellowColor: Int,
    purpleColor: Int,
    tealColor: Int,
) {
    val datasetList = mutableListOf<ILineDataSet>()
    
    // Adjust y-axis range
    val yAxis = axisLeft
    yAxis.axisMaximum = upperLimit
    yAxis.axisMinimum = if (glucoseData.isNotEmpty()) lowerLimit else 0f
    
    // Convert data to convenient format
    val glucoseDataEntries = glucoseData.map { glucose ->
        if (glucose.level > yAxis.axisMaximum) yAxis.axisMaximum = glucose.level
        if (glucose.level < yAxis.axisMinimum) yAxis.axisMinimum = glucose.level
        
        val dateTime = glucose.dateTime.toDateTime(DateFormat.RAW_FULL)
        Entry(dateTime.timeMillis().toFloat(), glucose.level)
    }
    
    yAxis.axisMinimum -= 20
    yAxis.axisMaximum += 20
    
    // Create data set for glucose level chart
    val glucoseDataset = LineDataSet(
        glucoseDataEntries,
        context.getString(R.string.txt_blood_glucose_glucosechart)
    ).apply {
        setDrawFilled(false) // Disable area fill
        
        // Set line style
        color = primaryColor.alpha(0.5f)
        lineWidth = 2f
        enableDashedLine(10f, 5f, 0f)
        
        // Set value point style
        setCircleColor(primaryColor.alpha(0.75f))
        circleRadius = 2.5f
        setDrawCircleHole(false)
        
        // Set highlight style
        highlightLineWidth = 1f
        highLightColor = primaryColor.alpha(0.5f)
        enableDashedHighlightLine(10f, 5f, 0f)
    }
    datasetList.add(glucoseDataset)
    
    if (eventData?.isNotEmpty() == true) {
        val groupedEventData = eventData.groupBy { it.type }
        groupedEventData.forEach { (type, events) ->
            val datasetLabel = context.getString(
                when (type) {
                    EventType.MEAL -> R.string.event_type_option_meal
                    EventType.EXERCISE -> R.string.event_type_option_exercise
                    EventType.MEDICATION -> R.string.event_type_option_medication
                }
            )
            val datasetColor = when (type) {
                EventType.MEAL -> yellowColor
                EventType.EXERCISE -> purpleColor
                EventType.MEDICATION -> tealColor
            }
            
            val eventDataEntries = events.map { event ->
                val dateTime = event.dateTime.toDateTime(DateFormat.RAW_FULL)
                Entry(
                    dateTime.timeMillis().toFloat(),
                    event.initialGlucose.orInvalid().toFloat(),
                    event,
                )
            }
            val eventDataset = LineDataSet(eventDataEntries, datasetLabel).apply {
                setDrawFilled(false)
                
                color = datasetColor
                enableDashedLine(0f, 1f, 0f)
                
                setCircleColor(datasetColor)
                circleRadius = 2.5f
                setDrawCircleHole(false)
                
                highlightLineWidth = 1f
                highLightColor = datasetColor
                enableDashedHighlightLine(10f, 5f, 0f)
            }
            datasetList.add(eventDataset)
        }
    }
    
    // Show all data sets to the chart
    val chartData = LineData(datasetList).apply {
        setDrawValues(false) // Disable value labels, too much data
    }
    data = chartData
    
    if (glucoseData.isNotEmpty()) {
        // Adjust the x-axis range based on glucose data
        // Set min and max based on the first and last date-time
        xAxis.axisMinimum = glucoseData.first().dateTime.toDateTime(DateFormat.RAW_FULL)
            .minusHours(1) // Add space before the first data
            .timeMillis().toFloat()
        xAxis.axisMaximum = glucoseData.last().dateTime.toDateTime(DateFormat.RAW_FULL)
            .plusHours(1) // Add space after the last data
            .timeMillis().toFloat()
    } else if (eventData?.isNotEmpty() == true) {
        // Adjust the x-axis range if glucose data is empty, but event data is not
        // Set min and max to 00.00 and 23.59
        xAxis.axisMinimum = eventData.first().dateTime.toDateTime(DateFormat.RAW_FULL)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .timeMillis().toFloat()
        xAxis.axisMaximum = eventData.last().dateTime.toDateTime(DateFormat.RAW_FULL)
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999)
            .timeMillis().toFloat()
    } else {
        // Adjust the x-axis range if both glucose and event data are empty
        // Set min and max to 00.00 and 23.59 (today)
        xAxis.axisMinimum = getCurrentDateTime()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .timeMillis().toFloat()
        xAxis.axisMaximum = getCurrentDateTime()
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999)
            .timeMillis().toFloat()
    }
    
    // Fix library bug -> throw NullPointerException when getDataSetIndex on multiple datasets
    setDrawMarkers(false)
    
    invalidate()
    
    // Animate displaying data
    animateX(800)
}

@Preview(showBackground = true)
@Composable
private fun GlucoseChartPreview() {
    GlucoverTheme {
        GlucoseChart(
            title = R.string.data_unavailable.toText(),
            glucoseData = listOf(),
        )
    }
}