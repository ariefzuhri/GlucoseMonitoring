package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.DateFormat
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.displayGlucoseChanges
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.spacedPlus
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toFuzzyDate
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toReadableTime
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun EventItem(
    type: EventType,
    desc: String,
    date: String,
    time: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glucoseChanges: Int? = null,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(24.dp),
    ) {
        val context = LocalContext.current
        val (imgIcon, txtDateTime, txtDesc, txtGlucoseChanges, txtGlucoseUnit) = createRefs()
        
        Icon(
            icon = when (type) {
                EventType.MEAL -> GlucoverIcons.Meal
                EventType.EXERCISE -> GlucoverIcons.Exercise
                EventType.MEDICATION -> GlucoverIcons.Medication
            },
            modifier = Modifier
                .constrainAs(imgIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )
        
        Text(
            text = date.toFuzzyDate(context, DateFormat.RAW_DATE, true)
                .spacedPlus(time.toReadableTime(DateFormat.RAW_TIME), true),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .constrainAs(txtDateTime) {
                    top.linkTo(parent.top)
                    start.linkTo(imgIcon.end, 20.dp)
                    if (type == EventType.MEAL) end.linkTo(txtGlucoseUnit.start, 16.dp)
                    else end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
        
        Text(
            text = when (type) {
                EventType.MEAL -> R.string.event_type_option_meal
                EventType.EXERCISE -> R.string.event_type_option_exercise
                EventType.MEDICATION -> R.string.event_type_option_medication
            }.toText().spacedPlus(desc.lowercase()),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(txtDesc) {
                    top.linkTo(txtDateTime.bottom, 4.dp)
                    start.linkTo(txtDateTime.start)
                    end.linkTo(txtDateTime.end)
                    width = Dimension.fillToConstraints
                }
        )
        
        if (type == EventType.MEAL) {
            Text(
                text = displayGlucoseChanges(glucoseChanges),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .constrainAs(txtGlucoseChanges) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            
            Text(
                text = R.string.glucose_unit.toText(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                modifier = Modifier
                    .constrainAs(txtGlucoseUnit) {
                        top.linkTo(txtGlucoseChanges.bottom, 2.dp)
                        end.linkTo(txtGlucoseChanges.end)
                    }
            )
        }
    }
}

@Preview
@Composable
private fun EventItemSummary() {
    GlucoverTheme {
        EventItem(
            type = EventType.MEAL,
            desc = "Oatmeal",
            date = "2023-04-10",
            time = "06:00",
            glucoseChanges = 10,
            onClick = {},
        )
    }
}