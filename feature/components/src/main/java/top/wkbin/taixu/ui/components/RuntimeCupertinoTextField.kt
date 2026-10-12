package top.wkbin.taixu.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility
import io.github.alexzhirkevich.cupertino.CupertinoTextField
import androidx.compose.ui.res.stringResource
import top.wkbin.taixu.feature.components.R

@Composable
internal fun ChengmingTextField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier, enabled: Boolean,
    readOnly: Boolean, textStyle: TextStyle, label: (@Composable () -> Unit)?,
    placeholder: (@Composable () -> Unit)?, leadingIcon: (@Composable () -> Unit)?, trailingIcon: (@Composable () -> Unit)?,
    prefix: (@Composable () -> Unit)?, suffix: (@Composable () -> Unit)?, supportingText: (@Composable () -> Unit)?,
    isError: Boolean, visualTransformation: VisualTransformation, keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions, singleLine: Boolean, maxLines: Int, minLines: Int,
    interactionSource: MutableInteractionSource?, shape: Shape, colors: TextFieldColors,
) {
    val interaction = interactionSource ?: remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val scheme = MaterialTheme.colorScheme
    val errorLabel = stringResource(R.string.components_invalid_input)
    val foreground = when {
        !enabled -> colors.disabledTextColor
        isError -> colors.errorTextColor
        focused -> colors.focusedTextColor
        else -> colors.unfocusedTextColor
    }
    val accent = if (isError) scheme.error else scheme.primary
    val border = if (focused || isError || LocalChengmingAccessibility.current.increaseContrast)
        BorderStroke(if (focused) 2.dp else 1.dp, if (isError || focused) accent else scheme.outline) else null
    Column(modifier.semantics { if (isError) error(errorLabel) }, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (label != null) {
            CompositionLocalProvider(LocalContentColor provides scheme.onSurfaceVariant) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) { label() }
            }
        }
        Surface(shape = shape, color = scheme.surfaceContainerHigh, border = border,
            contentColor = foreground, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            CupertinoTextField(value = value, onValueChange = onValueChange, enabled = enabled,
                readOnly = readOnly, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                textStyle = MaterialTheme.typography.bodyLarge.merge(textStyle).copy(color = foreground),
                placeholder = placeholder?.let { slot -> { CupertinoContent { slot() } } },
                leadingIcon = if (leadingIcon == null && prefix == null) null else {
                    { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { leadingIcon?.invoke(); prefix?.invoke() } }
                },
                trailingIcon = if (trailingIcon == null && suffix == null) null else {
                    { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { suffix?.invoke(); trailingIcon?.invoke() } }
                },
                isError = isError, visualTransformation = visualTransformation, keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions, singleLine = singleLine, maxLines = maxLines, minLines = minLines,
                interactionSource = interaction)

        }
        if (supportingText != null) {
            CompositionLocalProvider(LocalContentColor provides if (isError) scheme.error else scheme.onSurfaceVariant) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) { supportingText() }
            }
        }
    }
}
