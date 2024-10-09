package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.Option
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.eventTypeOptions
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toPainter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SingleOptionGroup(
    options: List<Option>,
    onSelected: (Option) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option.isEnabled.value,
                shape = RoundedCornerShape(100),
                leadingIcon = {
                    if (option.icon != null) {
                        Icon(
                            painter = option.icon.toPainter(),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = option.displayName.toText(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(start = if (option.icon != null) 0.dp else 2.dp)
                            .padding(end = 2.dp)
                            .padding(vertical = 8.dp),
                    )
                },
                onClick = {
                    onSelected(option)
                },
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 5.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleOptionGroupPreview() {
    GlucoverTheme {
        SingleOptionGroup(
            options = eventTypeOptions,
            onSelected = {},
        )
    }
}