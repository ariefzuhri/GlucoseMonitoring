package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.TestTags
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.orNone
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun GlucoseStatsContainer(
    lastValue: Int?,
    average: Int?,
    max: Int?,
    min: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        StatComponent(
            name = R.string.txt_last_value_glucosestats,
            value = lastValue,
            testTag = TestTags.STATS_LAST_VALUE,
            modifier = Modifier.weight(1f),
        )
        StatComponent(
            name = R.string.txt_average_glucosestats,
            value = average,
            testTag = TestTags.STATS_AVERAGE,
            modifier = Modifier.weight(1f),
        )
        StatComponent(
            name = R.string.txt_max_glucosestats,
            value = max,
            testTag = TestTags.STATS_MAX,
            modifier = Modifier.weight(1f),
        )
        StatComponent(
            name = R.string.txt_min_glucosestats,
            value = min,
            testTag = TestTags.STATS_MIN,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatComponent(
    @StringRes name: Int,
    value: Int?,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = value?.toString().orNone(),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag(testTag),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name.toText(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GlucoseStatsContainerPreview() {
    GlucoverTheme {
        GlucoseStatsContainer(
            lastValue = 90,
            average = 100,
            max = 180,
            min = 50,
            modifier = Modifier.padding(48.dp),
        )
    }
}