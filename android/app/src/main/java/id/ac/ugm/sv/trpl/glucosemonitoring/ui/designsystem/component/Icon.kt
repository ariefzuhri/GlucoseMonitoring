package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toPainter
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme
import androidx.compose.material3.Icon as MaterialIcon

@Composable
fun Icon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    @StringRes contentDescription: Int? = null,
    color: Color = MaterialTheme.colorScheme.primary.copy(0.75f),
) {
    MaterialIcon(
        painter = icon.toPainter(),
        contentDescription = contentDescription?.toText(),
        tint = color,
        modifier = modifier
            .size(size),
    )
}

@Preview(showBackground = true)
@Composable
private fun IconPreview() {
    GlucoverTheme {
        Icon(GlucoverIcons.Glucose)
    }
}