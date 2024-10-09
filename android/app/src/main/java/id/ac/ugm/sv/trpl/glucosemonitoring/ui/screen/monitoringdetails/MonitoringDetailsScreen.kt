package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.monitoringdetails

import android.util.Range
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.orNone
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.MonitoringDetailsPeriodFilter
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.TirStatus
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.GlucoseStats
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.MonitoringDetails.Tir
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Recommendations.Recommendation
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ActionBar
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.CalendarDialog
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.GlucoseChart
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.GlucoseStatsContainer
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SingleOptionGroup
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.TirChart
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.green
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.orange
import java.time.LocalDate

@Composable
fun MonitoringDetailsScreen(
    navigateToExpandedChart: (List<Glucose>, List<Event>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MonitoringDetailsViewModel = hiltViewModel(),
) {
    MonitoringDetailsContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onSelectFilter = viewModel::onSelectFilter,
        onSelectCustomPeriod = viewModel::onSelectCustomPeriod,
        navigateToExpandedChart = navigateToExpandedChart,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun MonitoringDetailsContent(
    state: MonitoringDetailsState,
    onSelectFilter: (Option) -> Unit,
    onSelectCustomPeriod: (LocalDate, LocalDate) -> Unit,
    navigateToExpandedChart: (List<Glucose>, List<Event>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val calendarDialog = rememberUseCaseState()
    CalendarDialog(
        state = calendarDialog,
        period = Range(state.customPeriod.start, state.customPeriod.endInclusive),
        onSelectPeriod = onSelectCustomPeriod,
    )
    
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        ActionBar(
            topTitle = R.string.txt_top_title_monitoringdetails.toText(),
            bottomTitle = R.string.txt_bottom_title_monitoringdetails.toText(),
            icon = GlucoverIcons.MonitoringDetails,
            action = onBack,
        )
        
        Spacer(modifier = Modifier.height(72.dp))
        
        SingleOptionGroup(
            options = state.periodFilters,
            onSelected = {
                if (it.name == MonitoringDetailsPeriodFilter.CUSTOM.name) calendarDialog.show()
                else onSelectFilter(it)
            }
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        ChartSection(
            selectedFilter = state.selectedPeriodFilter,
            selectedPeriod = state.customPeriod,
            glucoseData = state.glucoseData,
            eventData = state.eventData,
            glucoseStats = state.glucoseStats,
            navigateToExpandedChart = navigateToExpandedChart,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        TirSection(
            tir = state.tir,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        RecommendationsSection(
            recommendations = state.recommendations,
        )
    }
}

@Composable
private fun ChartSection(
    selectedFilter: Option,
    selectedPeriod: ClosedRange<LocalDate>,
    glucoseData: List<Glucose>,
    eventData: List<Event>,
    glucoseStats: GlucoseStats,
    navigateToExpandedChart: (List<Glucose>, List<Event>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val chartTitle = if (glucoseData.isNotEmpty()) {
            stringResource(
                R.string.txt_title_glucose_chart_monitoringdetails,
                if (selectedFilter.name == MonitoringDetailsPeriodFilter.CUSTOM.name) {
                    selectedPeriod.start.toString(DateFormat.READABLE_LONG_DATE).plus("—")
                        .plus(selectedPeriod.endInclusive.toString(DateFormat.READABLE_LONG_DATE))
                } else {
                    selectedFilter.displayName.toText().lowercase()
                },
            )
        } else {
            R.string.data_unavailable.toText()
        }
        
        GlucoseChart(
            title = chartTitle,
            glucoseData = glucoseData,
            eventData = eventData,
            navigateToExpandedChart = { navigateToExpandedChart(glucoseData, eventData) },
            modifier = Modifier.testTag(TestTags.GLUCOSE_CHART),
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        GlucoseStatsContainer(
            lastValue = glucoseStats.lastValue,
            average = glucoseStats.average,
            max = glucoseStats.max,
            min = glucoseStats.min,
        )
    }
}

@Composable
private fun TirSection(
    tir: Tir,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val isTirDataAvailable = tir.timeInRange != null
        
        Text(
            text = R.string.txt_tir_monitoringdetails.toText(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = R.string.txt_tir_value_monitoringdetails.toText(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = tir.timeInRange?.let { "$it%" }.orNone(),
                style = MaterialTheme.typography.displaySmall,
                color = if (isTirDataAvailable) {
                    val meetTarget = TirStatus.IN_RANGE.range.contains(tir.timeInRange!!)
                    if (meetTarget) MaterialTheme.colorScheme.green
                    else MaterialTheme.colorScheme.orange
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(0.4f)
                },
                modifier = Modifier.testTag(TestTags.TIR_PERCENTAGE),
            )
        }
        
        if (isTirDataAvailable) {
            Spacer(modifier = Modifier.height(16.dp))
            TirChart(
                timeInRange = tir.timeInRange!!,
                timeAboveRange = tir.timeAboveRange!!,
                timeBelowRange = tir.timeBelowRange!!,
            )
        }
    }
}

@Composable
private fun RecommendationsSection(
    recommendations: Recommendations,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = R.string.txt_recommendations_monitoringdetails.toText(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (!recommendations.isEmpty) {
            val recommendationList = listOfNotNull(
                recommendations.glucoseRecommendation,
                recommendations.bloodPressureRecommendation,
                recommendations.bmiRecommendation,
            )
            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                recommendationList.forEach { RecommendationContainer(it) }
            }
        } else {
            Text(
                text = R.string.data_unavailable.toText(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
            )
        }
    }
}

@Composable
private fun RecommendationContainer(
    recommendation: Recommendation,
    modifier: Modifier = Modifier,
) {
    val testTag = when (recommendation) {
        is Recommendations.GlucoseRecommendation -> TestTags.GLUCOSE_RECOMMENDATION
        is Recommendations.BloodPressureRecommendation -> TestTags.BLOOD_PRESSURE_RECOMMENDATION
        is Recommendations.BmiRecommendation -> TestTags.BMI_RECOMMENDATION
    }
    
    Column(
        modifier = modifier,
    ) {
        Text(
            text = "${recommendation.type}: ${recommendation.value} (${recommendation.status})",
            style = MaterialTheme.typography.titleSmall,
            color = if (recommendation.meetTarget) MaterialTheme.colorScheme.green
            else MaterialTheme.colorScheme.orange,
            modifier = modifier.padding(bottom = 8.dp),
        )
        Text(
            text = stringResource(
                R.string.txt_recommended_value_monitoringdetails,
                recommendation.recommendedValue,
            ),
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = recommendation.getRecommendation(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 20.dp)
                    .testTag(testTag),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    GlucoverTheme {
        MonitoringDetailsScreen(
            navigateToExpandedChart = { _, _ -> },
            onBack = {},
        )
    }
}