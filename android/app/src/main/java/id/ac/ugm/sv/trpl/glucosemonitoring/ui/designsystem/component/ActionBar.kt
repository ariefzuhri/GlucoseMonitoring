package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun ActionBar(
    action: () -> Unit,
    modifier: Modifier = Modifier,
    topTitle: String? = null,
    bottomTitle: String? = null,
    @DrawableRes icon: Int? = null,
    content: @Composable () -> Unit = {
        ActionBarContent(
            topTitle = topTitle,
            bottomTitle = bottomTitle,
            icon = icon,
            onBackClick = action,
            modifier = modifier,
        )
    },
) {
    content()
}

@Composable
private fun ActionBarContent(
    topTitle: String?,
    bottomTitle: String?,
    @DrawableRes icon: Int?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Icon(
            icon = GlucoverIcons.Back,
            color = MaterialTheme.colorScheme.onBackground.copy(0.2f),
            contentDescription = R.string.btn_cd_back,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onBackClick() },
        )
        if (icon != null || topTitle != null && bottomTitle != null) {
            Spacer(modifier = Modifier.width(32.dp))
            Column {
                if (icon != null) {
                    Icon(icon)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (topTitle != null && bottomTitle != null) {
                    Text(
                        text = topTitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = bottomTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionBarPreview() {
    GlucoverTheme {
        ActionBar(
            topTitle = R.string.txt_top_title_monitoringdetails.toText(),
            bottomTitle = R.string.txt_bottom_title_monitoringdetails.toText(),
            icon = GlucoverIcons.MonitoringDetails,
            action = {},
            modifier = Modifier.padding(40.dp),
        )
    }
}