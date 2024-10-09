package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun HintBox(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp),
        )
    }
}

@Preview
@Composable
private fun HintBoxPreview() {
    GlucoverTheme {
        HintBox(
            R.string.txt_disclaimer_healthnumbersform.toText()
        )
    }
}