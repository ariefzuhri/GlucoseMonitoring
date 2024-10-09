package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.getCurrentDateTime
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toStringOrNone
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.displayGlucoseChanges
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.spacedPlus
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toFuzzyDate
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toReadableTime
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Divider
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.GlucoseChart
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.HintBox
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.Icon
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ImageButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.PrimaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SecondaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SingleLineEventItem
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.green
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.orange
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.red

@Composable
fun HomeScreen(
    navigateToProfile: () -> Unit,
    navigateToMonitoringDetails: () -> Unit,
    navigateToAddEventForm: () -> Unit,
    navigateToEventDetails: (Event) -> Unit,
    navigateToEventList: () -> Unit,
    navigateToExpandedChart: (List<Glucose>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToProfile = navigateToProfile,
        navigateToMonitoringDetails = navigateToMonitoringDetails,
        navigateToAddEventForm = navigateToAddEventForm,
        navigateToEventDetails = navigateToEventDetails,
        navigateToEventList = navigateToEventList,
        navigateToExpandedChart = navigateToExpandedChart,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    navigateToProfile: () -> Unit,
    navigateToMonitoringDetails: () -> Unit,
    navigateToAddEventForm: () -> Unit,
    navigateToEventDetails: (Event) -> Unit,
    navigateToEventList: () -> Unit,
    navigateToExpandedChart: (List<Glucose>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        ProfileSection(
            userName = state.userName,
            navigateToProfile = navigateToProfile,
        )
        
        Spacer(modifier = Modifier.height(72.dp))
        
        if (state.monitoringSummary.latestGlucoseCategory != GlucoseCategory.UNKNOWN) {
            GlucoseLatestSection(
                latestGlucoseCategory = state.monitoringSummary.latestGlucoseCategory,
                latestGlucoseLevel = state.monitoringSummary.latestGlucoseLevel,
                lastSeenGlucoseChanges = state.monitoringSummary.lastSeenGlucoseChanges,
                lastSeenGlucoseTime = state.monitoringSummary.lastSeenGlucoseTime,
            )
            
            Spacer(modifier = Modifier.height(64.dp))
        }
        
        GlucoseChartSection(
            latestGlucoseTime = state.monitoringSummary.latestGlucoseTime,
            latestGlucoseData = state.monitoringSummary.latestGlucoseData,
            navigateToMonitoringDetails = navigateToMonitoringDetails,
            navigateToExpandedChart = navigateToExpandedChart,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        LastEventsSection(
            lastMeal = state.monitoringSummary.lastMeal,
            lastExercise = state.monitoringSummary.lastExercise,
            lastMedication = state.monitoringSummary.lastMedication,
            navigateToAddEventForm = navigateToAddEventForm,
            navigateToEventDetails = navigateToEventDetails,
            navigateToEventList = navigateToEventList,
        )
    }
}

@Composable
private fun ProfileSection(
    userName: String,
    navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        ImageButton(
            icon = GlucoverIcons.Profile,
            contentDescription = R.string.btn_cd_profile_home,
            onClick = navigateToProfile,
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column(
            modifier = modifier,
        ) {
            Text(
                text = R.string.txt_hello_home.toText(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun GlucoseLatestSection(
    latestGlucoseCategory: GlucoseCategory,
    latestGlucoseLevel: Int?,
    lastSeenGlucoseChanges: Int?,
    lastSeenGlucoseTime: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val latestGlucoseCategoryString = stringResource(
            R.string.txt_latest_glucose_category_home,
            when (latestGlucoseCategory) {
                GlucoseCategory.HYPERGLYCEMIA_LEVEL_2 -> R.string.glucose_category_hyperglycemia_level_2
                GlucoseCategory.HYPERGLYCEMIA_LEVEL_1 -> R.string.glucose_category_hyperglycemia_level_1
                GlucoseCategory.IN_RANGE -> R.string.glucose_category_in_range
                GlucoseCategory.HYPOGLYCEMIA_LEVEL_1 -> R.string.glucose_category_hypoglycemia_level_2
                GlucoseCategory.HYPOGLYCEMIA_LEVEL_2 -> R.string.glucose_category_hypoglycemia_level_1
                else -> R.string.glucose_category_unknown
            }.toText().lowercase()
        )
        val latestGlucoseCategoryColor = when (latestGlucoseCategory) {
            GlucoseCategory.HYPERGLYCEMIA_LEVEL_2 -> MaterialTheme.colorScheme.red
            GlucoseCategory.HYPERGLYCEMIA_LEVEL_1 -> MaterialTheme.colorScheme.orange
            GlucoseCategory.IN_RANGE -> MaterialTheme.colorScheme.green
            GlucoseCategory.HYPOGLYCEMIA_LEVEL_1 -> MaterialTheme.colorScheme.orange
            GlucoseCategory.HYPOGLYCEMIA_LEVEL_2 -> MaterialTheme.colorScheme.red
            else -> MaterialTheme.colorScheme.onBackground
        }
        
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            val (imgIcon, txtValue, txtUnit, txtLastSeen) = createRefs()
            
            Icon(
                icon = GlucoverIcons.Glucose,
                contentDescription = R.string.txt_cd_latest_glucose_category_home,
                color = latestGlucoseCategoryColor,
                modifier = Modifier
                    .constrainAs(imgIcon) {
                        top.linkTo(txtValue.top)
                        bottom.linkTo(txtValue.bottom)
                        start.linkTo(parent.start)
                    },
            )
            Text(
                text = latestGlucoseLevel.toStringOrNone(),
                style = MaterialTheme.typography.displayLarge,
                color = latestGlucoseCategoryColor,
                modifier = Modifier
                    .constrainAs(txtValue) {
                        top.linkTo(parent.top)
                        start.linkTo(imgIcon.end, 4.dp)
                    },
            )
            Text(
                text = R.string.glucose_unit.toText(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                modifier = Modifier
                    .constrainAs(txtUnit) {
                        top.linkTo(txtValue.bottom, (-4).dp)
                        end.linkTo(txtValue.end)
                    },
            )
            
            val isLastSeenDataAvailable = lastSeenGlucoseChanges != null
            if (isLastSeenDataAvailable) {
                HintBox(
                    text = stringResource(
                        R.string.txt_last_seen_glucose_home,
                        displayGlucoseChanges(lastSeenGlucoseChanges),
                        lastSeenGlucoseTime!!.toReadableTime(DateFormat.RAW_FULL)
                    ),
                    modifier = Modifier
                        .constrainAs(txtLastSeen) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(txtValue.end, 32.dp)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                )
            }
        }
        
        Text(
            text = latestGlucoseCategoryString,
            style = MaterialTheme.typography.headlineSmall,
            color = latestGlucoseCategoryColor,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun GlucoseChartSection(
    latestGlucoseTime: String?,
    latestGlucoseData: List<Glucose>,
    navigateToMonitoringDetails: () -> Unit,
    navigateToExpandedChart: (List<Glucose>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val isLatestGlucoseDataAvailable = latestGlucoseData.isNotEmpty()
        val glucoseChartTitle = if (isLatestGlucoseDataAvailable) {
            stringResource(
                R.string.txt_title_glucose_chart_home,
                getCurrentDateTime().toString(DateFormat.READABLE_SHORT_DATE),
                latestGlucoseTime!!.toReadableTime(DateFormat.RAW_TIME)
            )
        } else {
            R.string.data_unavailable.toText()
        }
        
        GlucoseChart(
            title = glucoseChartTitle,
            glucoseData = latestGlucoseData,
            navigateToExpandedChart = { navigateToExpandedChart(latestGlucoseData) },
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SecondaryButton(
            title = R.string.btn_details_home,
            icon = GlucoverIcons.Next,
            onClick = navigateToMonitoringDetails,
            modifier = Modifier.align(Alignment.End),
        )
    }
}

@Composable
private fun LastEventsSection(
    lastMeal: Event?,
    lastExercise: Event?,
    lastMedication: Event?,
    navigateToAddEventForm: () -> Unit,
    navigateToEventDetails: (Event) -> Unit,
    navigateToEventList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = R.string.tv_last_events_home.toText(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
            )
            PrimaryButton(
                title = R.string.btn_add_event_home,
                icon = GlucoverIcons.Add,
                onClick = navigateToAddEventForm,
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LastMealItem(
            lastMeal = lastMeal,
            onClick = { lastMeal?.let { navigateToEventDetails(it) } },
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            lastExercise?.let {
                SingleLineEventItem(
                    type = it.type,
                    desc = it.desc,
                    date = it.date,
                    time = it.time,
                    onClick = { navigateToEventDetails(it) },
                )
            }
            lastMedication?.let {
                SingleLineEventItem(
                    type = it.type,
                    desc = it.desc,
                    date = it.date,
                    time = it.time,
                    onClick = { navigateToEventDetails(it) },
                )
            }
        }
        
        Divider(modifier = Modifier.padding(vertical = 32.dp))
        
        SecondaryButton(
            title = R.string.btn_event_list_home,
            icon = GlucoverIcons.Next,
            onClick = navigateToEventList,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun LastMealItem(
    lastMeal: Event?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val isDataAvailable = lastMeal != null
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = isDataAvailable, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        Icon(
            icon = GlucoverIcons.Meal,
            size = 32.dp,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = if (isDataAvailable) lastMeal?.date?.toFuzzyDate(context, DateFormat.RAW_DATE)
                .spacedPlus(lastMeal?.time?.toReadableTime(DateFormat.RAW_TIME), true)
            else R.string.data_unavailable.toText(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = displayGlucoseChanges(lastMeal?.glucoseChanges),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = R.string.glucose_unit.toText(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    GlucoverTheme {
        HomeScreen(
            navigateToProfile = {},
            navigateToMonitoringDetails = {},
            navigateToAddEventForm = {},
            navigateToEventDetails = {},
            navigateToEventList = {},
            navigateToExpandedChart = {},
        )
    }
}