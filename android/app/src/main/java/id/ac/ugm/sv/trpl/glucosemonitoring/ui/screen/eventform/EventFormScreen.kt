package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.eventform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.toString
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper.Result
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.showToast
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.spacedPlus
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toFuzzyDate
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toPainter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toReadableTime
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ActionBar
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.CalendarDialog
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ClockDialog
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.PrimaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.SingleOptionGroup
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.TextField
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun EventFormScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    id: Int? = null,
    viewModel: EventFormViewModel = hiltViewModel(),
) {
    LaunchedEffect(id) {
        viewModel.selectEvent(id)
    }
    
    EventFormContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onTypeChange = viewModel::onTypeChange,
        onDescChange = viewModel::onDescChange,
        onSelectDate = viewModel::onSelectDate,
        onSelectTime = viewModel::onSelectTime,
        onSave = viewModel::onSave,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
fun EventFormContent(
    state: EventFormState,
    onTypeChange: (Option) -> Unit,
    onDescChange: (String) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onSelectTime: (LocalTime) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        ActionBar(
            topTitle = if (!state.isEdit) R.string.txt_top_title_add_eventform.toText()
            else R.string.txt_top_title_edit_eventform.toText(),
            bottomTitle = if (!state.isEdit) R.string.txt_bottom_title_add_eventform.toText()
            else R.string.txt_bottom_title_edit_eventform.toText(),
            icon = GlucoverIcons.EventForm,
            action = onBack
        )
        
        Spacer(modifier = Modifier.height(72.dp))
        
        TypeSection(
            typeOptions = state.typeOptions,
            onTypeChange = onTypeChange,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        DescSection(
            type = state.type,
            desc = state.desc,
            onDescChange = onDescChange,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        DateTimeSection(
            date = state.date,
            time = state.time,
            onSelectDate = onSelectDate,
            onSelectTime = onSelectTime,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        ActionButtonSection(
            addResult = state.addResult,
            editResult = state.editResult,
            onSave = onSave,
        )
    }
    
    ResultToast(
        addResult = state.addResult,
        editResult = state.editResult,
        onBack = onBack,
    )
}

@Composable
private fun TypeSection(
    typeOptions: List<Option>,
    onTypeChange: (Option) -> Unit,
) {
    Column {
        Text(
            text = R.string.txt_type_eventform.toText(),
            style = MaterialTheme.typography.titleLarge,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SingleOptionGroup(
            options = typeOptions,
            onSelected = onTypeChange,
        )
    }
}

@Composable
private fun DescSection(
    type: EventType,
    desc: String,
    onDescChange: (String) -> Unit,
) {
    Column {
        Text(
            text = when (type) {
                EventType.MEAL -> R.string.txt_desc_meal_eventform
                EventType.EXERCISE -> R.string.txt_desc_exercise_eventform
                EventType.MEDICATION -> R.string.txt_desc_medication_eventform
            }.toText(),
            style = MaterialTheme.typography.titleLarge,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        TextField(
            text = desc,
            icon = GlucoverIcons.EventDesc,
            placeholder = when (type) {
                EventType.MEAL -> R.string.edt_placeholder_desc_meal_eventform
                EventType.EXERCISE -> R.string.edt_placeholder_desc_exercise_eventform
                EventType.MEDICATION -> R.string.edt_placeholder_desc_medication_eventform
            },
            helper = when (type) {
                EventType.MEAL -> R.string.edt_helper_desc_meal_eventform
                EventType.EXERCISE -> R.string.edt_helper_desc_exercise_eventform
                EventType.MEDICATION -> R.string.edt_helper_desc_medication_eventform
            },
            onTextChange = onDescChange,
            capitalization = KeyboardCapitalization.Sentences,
            isMultiline = true,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DateTimeSection(
    date: LocalDate,
    time: LocalTime,
    onSelectDate: (LocalDate) -> Unit,
    onSelectTime: (LocalTime) -> Unit,
) {
    val context = LocalContext.current
    
    val calendarDialog = rememberUseCaseState()
    CalendarDialog(
        state = calendarDialog,
        date = date,
        onSelectDate = onSelectDate,
    )
    
    val clockDialog = rememberUseCaseState()
    ClockDialog(
        state = clockDialog,
        time = time,
        onSelectTime = onSelectTime,
    )
    
    Column {
        Text(
            text = R.string.txt_datetime_eventform.toText(),
            style = MaterialTheme.typography.titleLarge,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AssistChip(
            leadingIcon = {
                Icon(
                    painter = GlucoverIcons.DateRange.toPainter(),
                    contentDescription = R.string.chip_cd_datetime_eventform.toText(),
                    modifier = Modifier.padding(start = 10.dp)
                )
            },
            shape = RoundedCornerShape(100),
            onClick = {
                clockDialog.show()
                calendarDialog.show()
            },
            label = {
                Text(
                    text = date.toString(DateFormat.RAW_DATE)
                        .toFuzzyDate(context, DateFormat.RAW_DATE, true)
                        .spacedPlus(
                            time.toString(DateFormat.RAW_TIME).toReadableTime(DateFormat.RAW_TIME),
                            true
                        ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .padding(vertical = 12.dp),
                )
            },
            modifier = Modifier.testTag(TestTags.INPUT_TIME),
        )
    }
}

@Composable
private fun ActionButtonSection(
    addResult: Result<Nothing>,
    editResult: Result<Nothing>,
    onSave: () -> Unit,
) {
    Column {
        PrimaryButton(
            title = R.string.btn_save_eventform,
            icon = GlucoverIcons.Save,
            isLoading = addResult is Result.Loading || editResult is Result.Loading,
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ResultToast(
    addResult: Result<Nothing>,
    editResult: Result<Nothing>,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(addResult) {
        when (addResult) {
            is Result.Success -> {
                context.showToast(R.string.toast_success_add_eventform)
                onBack()
            }
            
            is Result.Failed -> {
                context.showToast(R.string.toast_failed_add_eventform)
            }
            
            else -> {}
        }
    }
    LaunchedEffect(editResult) {
        when (editResult) {
            is Result.Success -> {
                context.showToast(R.string.toast_success_edit_eventform)
                onBack()
            }
            
            is Result.Failed -> {
                context.showToast(R.string.toast_failed_edit_eventform)
            }
            
            else -> {}
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EventFormScreenPreview() {
    GlucoverTheme {
        EventFormScreen(
            onBack = {},
        )
    }
}