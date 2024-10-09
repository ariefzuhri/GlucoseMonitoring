package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.spacedPlus
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toFuzzyDate
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toReadableTime
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun SingleLineEventItem(
    type: EventType,
    desc: String,
    date: String,
    time: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .clickable { onClick() },
    ) {
        Icon(
            icon = when (type) {
                EventType.MEAL -> GlucoverIcons.Meal
                EventType.EXERCISE -> GlucoverIcons.Exercise
                EventType.MEDICATION -> GlucoverIcons.Medication
            }
        )
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 16.dp, end = 24.dp)
                .weight(1f),
        )
        Text(
            text = date.toFuzzyDate(context, DateFormat.RAW_DATE)
                .spacedPlus(time.toReadableTime(DateFormat.RAW_TIME), true),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleLineEventItemPreview() {
    GlucoverTheme {
        SingleLineEventItem(
            type = EventType.MEDICATION,
            desc = "Metformin",
            date = "2023-03-31",
            time = "12:00",
            onClick = {},
        )
    }
}