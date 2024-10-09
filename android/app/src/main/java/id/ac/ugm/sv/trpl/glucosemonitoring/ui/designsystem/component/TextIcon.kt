package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun TextIcon(
    text: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(icon)
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextIconPreview() {
    GlucoverTheme {
        TextIcon(
            text = "120/80 mmHg",
            icon = GlucoverIcons.BloodPressure,
        )
    }
}