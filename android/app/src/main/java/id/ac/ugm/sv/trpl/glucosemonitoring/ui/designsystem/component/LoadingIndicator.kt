package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        LinearProgressIndicator(
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .fillMaxWidth(1 / 4f)
                .heightIn(min = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingIndicatorPreview() {
    GlucoverTheme {
        LoadingIndicator(modifier = Modifier.padding(16.dp))
    }
}