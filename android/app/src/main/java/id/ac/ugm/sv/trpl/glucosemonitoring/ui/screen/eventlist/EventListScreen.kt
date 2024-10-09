package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ActionBar
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.EventItem
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SecondaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun EventListScreen(
    navigateToEventDetails: (Event) -> Unit,
    navigateToAddEventForm: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel = hiltViewModel(),
) {
    EventListContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToEventDetails = navigateToEventDetails,
        navigateToAddEventForm = navigateToAddEventForm,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun EventListContent(
    state: EventListState,
    navigateToEventDetails: (Event) -> Unit,
    navigateToAddEventForm: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(40.dp),
        modifier = modifier.testTag("EventListContent"),
    ) {
        item {
            ActionBar(
                topTitle = R.string.txt_top_title_eventlist.toText(),
                bottomTitle = R.string.txt_bottom_title_eventlist.toText(),
                icon = GlucoverIcons.EventList,
                action = onBack,
            )
            
            Spacer(modifier = Modifier.height(72.dp))
            
            SecondaryButton(
                title = R.string.btn_add_event_eventlist,
                icon = GlucoverIcons.Add,
                onClick = navigateToAddEventForm,
                modifier = Modifier.fillMaxWidth(),
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        val isDataAvailable = state.eventData.isNotEmpty()
        if (isDataAvailable) {
            items(state.eventData, key = { it.id }) { item ->
                EventItem(
                    type = item.type,
                    desc = item.desc,
                    date = item.date,
                    time = item.time,
                    glucoseChanges = item.glucoseChanges,
                    onClick = {
                        navigateToEventDetails(item)
                    },
                )
            }
        } else {
            item {
                Text(
                    text = R.string.data_unavailable.toText(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventListScreenPreview() {
    GlucoverTheme {
        EventListScreen(
            onBack = {},
            navigateToAddEventForm = {},
            navigateToEventDetails = {},
        )
    }
}