package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventdetails

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.INVALID
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.displayGlucoseChanges
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.showToast
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.spacedPlus
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toFuzzyDate
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toReadableTime
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ActionBar
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.DangerButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Divider
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.GlucoseChart
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Icon
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SecondaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SingleLineEventItem
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun EventDetailsScreen(
    id: Int,
    navigateToEventDetails: (Event) -> Unit,
    navigateToEditEventForm: (Event) -> Unit,
    navigateToExpandedChart: (List<Glucose>, List<Event>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(id) {
        viewModel.getEventDetails(id)
    }
    
    EventDetailsContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToEventDetails = navigateToEventDetails,
        navigateToEditEventForm = navigateToEditEventForm,
        navigateToExpandedChart = navigateToExpandedChart,
        onDelete = viewModel::onDelete,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun EventDetailsContent(
    state: EventDetailsState,
    navigateToEventDetails: (Event) -> Unit,
    navigateToEditEventForm: (Event) -> Unit,
    navigateToExpandedChart: (List<Glucose>, List<Event>) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        ActionBar(
            topTitle = when (state.event.type) {
                EventType.MEAL -> R.string.event_type_option_meal
                EventType.EXERCISE -> R.string.event_type_option_exercise
                EventType.MEDICATION -> R.string.event_type_option_medication
            }.toText(),
            bottomTitle = state.event.date.toFuzzyDate(context, DateFormat.RAW_DATE, true)
                .spacedPlus(state.event.time.toReadableTime(DateFormat.RAW_TIME), true),
            icon = when (state.event.type) {
                EventType.MEAL -> GlucoverIcons.Meal
                EventType.EXERCISE -> GlucoverIcons.Exercise
                EventType.MEDICATION -> GlucoverIcons.Medication
            },
            action = onBack,
        )
        
        Spacer(modifier = Modifier.height(72.dp))
        
        DescSection(state.event.desc)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (state.event.type == EventType.MEAL && state.event.glucoseChanges != null) {
            GlucoseChangesSection(
                initialGlucose = state.event.initialGlucose!!,
                finalGlucose = state.event.finalGlucose!!,
                glucoseChanges = state.event.glucoseChanges!!,
            )
        }
        
        GlucoseChart(
            glucoseData = state.event.relatedGlucoseData,
            eventData = listOf(state.event),
            navigateToExpandedChart = {
                navigateToExpandedChart(
                    state.event.relatedGlucoseData,
                    listOf(state.event),
                )
            },
            modifier = Modifier.testTag(TestTags.GLUCOSE_CHART),
        )
        
        if (state.relatedEventData.isNotEmpty()) {
            Divider(modifier = Modifier.padding(vertical = 32.dp))
            
            RelatedEventsSection(
                relatedEventData = state.relatedEventData,
                type = state.event.type,
                navigateToEventDetails = navigateToEventDetails,
            )
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        ActionButtonSection(
            navigateToEditEventForm = { navigateToEditEventForm(state.event) },
            deleteResult = state.deleteResult,
            onDelete = onDelete,
        )
    }
    
    ResultToast(
        deleteResult = state.deleteResult,
        onBack = onBack,
    )
}

@Composable
private fun DescSection(desc: String) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = desc,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
        )
    }
}

@Composable
private fun GlucoseChangesSection(
    initialGlucose: Int,
    finalGlucose: Int,
    glucoseChanges: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = when {
                glucoseChanges > 0 -> R.string.txt_glucose_increase_eventdetails
                glucoseChanges < 0 -> R.string.txt_glucose_decrease_eventdetails
                else -> R.string.txt_glucose_no_change_eventdetails
            }.toText(),
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = initialGlucose.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
        )
        Icon(
            icon = GlucoverIcons.GlucoseAlter,
            size = 12.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Text(
            text = finalGlucose.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
        )
    }
    
    Row {
        Text(
            text = displayGlucoseChanges(glucoseChanges),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = R.string.glucose_unit.toText(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
            modifier = Modifier
                .padding(start = 4.dp, bottom = 8.dp)
                .align(Alignment.Bottom),
        )
    }
    
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun RelatedEventsSection(
    relatedEventData: List<Event>,
    type: EventType,
    navigateToEventDetails: (Event) -> Unit,
) {
    Text(
        text = R.string.txt_affected_by_eventdetails.toText(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
    )
    
    Spacer(modifier = Modifier.height(32.dp))
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        relatedEventData.forEach { item ->
            SingleLineEventItem(
                type = item.type,
                desc = item.desc,
                date = item.date,
                time = item.time,
                onClick = { navigateToEventDetails(item) },
            )
        }
    }
}

@Composable
fun ActionButtonSection(
    navigateToEditEventForm: () -> Unit,
    deleteResult: Result<Nothing>,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        SecondaryButton(
            title = R.string.btn_edit_eventdetails,
            icon = GlucoverIcons.EventForm,
            onClick = navigateToEditEventForm,
            isEnabled = deleteResult !is Result.Loading,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(10.dp))
        DangerButton(
            title = R.string.btn_delete_eventform,
            icon = GlucoverIcons.Delete,
            onClick = onDelete,
            isLoading = deleteResult is Result.Loading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ResultToast(
    deleteResult: Result<Nothing>,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(deleteResult) {
        when (deleteResult) {
            is Result.Success -> {
                context.showToast(R.string.toast_success_delete_eventform)
                onBack()
            }
            
            is Result.Failed -> {
                context.showToast(R.string.toast_failed_delete_eventform)
            }
            
            else -> {}
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EventDetailsScreenPreview() {
    GlucoverTheme {
        EventDetailsScreen(
            id = Int.INVALID,
            navigateToEventDetails = {},
            navigateToEditEventForm = {},
            onBack = {},
            navigateToExpandedChart = { _, _ -> },
        )
    }
}