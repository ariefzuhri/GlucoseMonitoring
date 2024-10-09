package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 1.dp,
) {
    Canvas(
        modifier = modifier.fillMaxWidth(),
    ) {
        drawRoundRect(
            color = color,
            style = Stroke(
                width = thickness.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(2.dp.toPx(), 2.dp.toPx()),
                    phase = 2.dp.toPx(),
                ),
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DividerPreview() {
    GlucoverTheme {
        Divider(modifier = Modifier.padding(16.dp))
    }
}