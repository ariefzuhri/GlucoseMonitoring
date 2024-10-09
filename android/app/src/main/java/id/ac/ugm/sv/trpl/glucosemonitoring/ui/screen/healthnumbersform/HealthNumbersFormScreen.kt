package id.ac.ugm.sv.trpl.glucosemonitoring.ui.screen.healthnumbersform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.ActionBar
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.HintBox
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.PrimaryButton
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component.TextField
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun HealthNumbersFormScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HealthNumbersFormViewModel = hiltViewModel(),
) {
    HealthNumbersFormContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onWeightChange = viewModel::onWeightChange,
        onHeightChange = viewModel::onHeightChange,
        onSystolicChange = viewModel::onSystolicChange,
        onDiastolicChange = viewModel::onDiastolicChange,
        save = viewModel::save,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun HealthNumbersFormContent(
    state: HealthNumbersFormState,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    save: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(40.dp),
    ) {
        ActionBar(
            topTitle = R.string.txt_top_title_healthnumbersform.toText(),
            bottomTitle = R.string.txt_bottom_title_healthnumbersform.toText(),
            icon = GlucoverIcons.HealthNumbers,
            action = onBack,
        )
        
        Spacer(modifier = Modifier.height(72.dp))
        
        HintBox(R.string.txt_disclaimer_healthnumbersform.toText())
        
        Spacer(modifier = Modifier.height(64.dp))
        
        Text(
            text = R.string.txt_blood_pressure_healthnumbersform.toText(),
            style = MaterialTheme.typography.titleLarge,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                text = state.systolic,
                icon = GlucoverIcons.BloodPressure,
                placeholder = R.string.edt_placeholder_systolic_healthnumbersform,
                helper = R.string.edt_helper_systolic_healthnumbersform,
                onTextChange = onSystolicChange,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier.weight(1f),
            )
            
            Text(
                text = "/",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.3f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(top = 8.dp),
            )
            
            TextField(
                text = state.diastolic,
                icon = GlucoverIcons.BloodPressure,
                placeholder = R.string.edt_placeholder_diastolic_healthnumbersform,
                helper = R.string.edt_helper_diastolic_healthnumbersform,
                onTextChange = onDiastolicChange,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier.weight(1f),
            )
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        Text(
            text = R.string.txt_weight_healthnumbersform.toText(),
            style = MaterialTheme.typography.titleLarge,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        TextField(
            text = state.weight,
            icon = GlucoverIcons.BodyWeight,
            placeholder = R.string.edt_placeholder_weight_healthnumbersform,
            onTextChange = onWeightChange,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        Text(
            text = R.string.txt_height_healthnumbersform.toText(),
            style = MaterialTheme.typography.titleLarge,
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        TextField(
            text = state.height,
            icon = GlucoverIcons.BodyHeight,
            placeholder = R.string.edt_placeholder_height_healthnumbersform,
            onTextChange = onHeightChange,
            keyboardType = KeyboardType.Number,
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        PrimaryButton(
            title = R.string.btn_save_healthnumbersform,
            icon = GlucoverIcons.Save,
            onClick = {
                save()
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HealthNumbersFormScreenPreview() {
    GlucoverTheme {
        HealthNumbersFormScreen(
            onBack = {},
        )
    }
}