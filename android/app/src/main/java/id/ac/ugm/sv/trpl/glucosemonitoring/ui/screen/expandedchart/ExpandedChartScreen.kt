package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.expandedchart

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.LockScreenOrientation
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.GlucoseChart
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun ExpandedChartScreen(
    glucoseData: List<Glucose>,
    eventData: List<Event>,
    modifier: Modifier = Modifier,
) {
    LockScreenOrientation(
        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
    )
    
    ExpandedChartContent(
        glucoseData = glucoseData,
        eventData = eventData,
        modifier = modifier,
    )
}

@Composable
private fun ExpandedChartContent(
    glucoseData: List<Glucose>,
    eventData: List<Event>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        GlucoseChart(
            glucoseData = glucoseData,
            eventData = eventData,
            isExpanded = true,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ExpandedChartScreenPreview() {
    GlucoverTheme {
        ExpandedChartScreen(
            glucoseData = emptyList(),
            eventData = emptyList(),
        )
    }
}