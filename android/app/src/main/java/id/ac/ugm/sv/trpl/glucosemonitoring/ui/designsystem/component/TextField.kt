package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.ac.ugm.sv.trpl.glucosemonitoring.R
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util.EMPTY
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util.toText
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.icon.GlucoverIcons
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme.GlucoverTheme

@Composable
fun TextField(
    text: String,
    @DrawableRes icon: Int,
    @StringRes placeholder: Int,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes helper: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isMultiline: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocus = interactionSource.collectIsFocusedAsState().value
    
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization,
            imeAction = imeAction,
        ),
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        singleLine = !isMultiline,
        maxLines = 4,
        textStyle = MaterialTheme.typography.labelMedium,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            TextFieldContainer(
                text = text,
                icon = icon,
                placeholder = placeholder,
                helper = helper,
                innerTextField = innerTextField,
                isFocus = isFocus,
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun TextFieldContainer(
    text: String,
    @DrawableRes icon: Int,
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier,
    @StringRes helper: Int? = null,
    innerTextField: @Composable () -> Unit,
    isFocus: Boolean,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(100),
                )
                .border(
                    width = 1.5.dp,
                    color = if (isFocus) MaterialTheme.colorScheme.outline else Color.Transparent,
                    shape = RoundedCornerShape(100),
                )
                .padding(vertical = 12.dp)
                .padding(start = 28.dp, end = 10.dp),
        ) {
            Icon(icon)
            Spacer(modifier = Modifier.width(16.dp))
            Box {
                innerTextField()
                if (text.isEmpty()) {
                    Text(
                        text = placeholder.toText(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                        softWrap = false,
                    )
                }
            }
        }
        
        if (helper != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = helper.toText(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    GlucoverTheme {
        TextField(
            text = String.EMPTY,
            icon = GlucoverIcons.Meal,
            placeholder = R.string.edt_placeholder_desc_meal_eventform,
            helper = R.string.edt_helper_desc_meal_eventform,
            keyboardType = KeyboardType.Text,
            onTextChange = {},
        )
    }
}