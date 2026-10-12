@file:OptIn(io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi::class)

package top.wkbin.taixu.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility
import top.wkbin.taixu.ui.theme.LocalLiquidGlassSurfaceBackdrop
import io.github.alexzhirkevich.cupertino.CupertinoButton
import io.github.alexzhirkevich.cupertino.CupertinoButtonDefaults
import io.github.alexzhirkevich.cupertino.CupertinoSwitch
import io.github.alexzhirkevich.cupertino.CupertinoSwitchDefaults
import io.github.alexzhirkevich.cupertino.CupertinoCheckBox
import io.github.alexzhirkevich.cupertino.CupertinoCheckboxDefaults

internal val LocalCupertinoChrome = staticCompositionLocalOf { false }

@Composable
internal fun ChengmingButton(
    onClick: () -> Unit, modifier: Modifier, enabled: Boolean,
    contentPadding: PaddingValues, shape: Shape, colors: ButtonColors,
    border: BorderStroke?, glass: Boolean = false, content: @Composable RowScope.() -> Unit,
) {
    val background = if (enabled) colors.containerColor else colors.disabledContainerColor
    val foreground = if (enabled) colors.contentColor else colors.disabledContentColor
    val canGlass = glass && enabled && LocalChengmingAccessibility.current.glassEnabled &&
        LocalLiquidGlassSurfaceBackdrop.current != null
    val slot: @Composable () -> Unit = {
        CompositionLocalProvider(LocalContentColor provides foreground) {
            ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                Row(Modifier.padding(contentPadding), Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    Alignment.CenterVertically, content = content)
            }
        }
    }
    val target = modifier.minimumInteractiveComponentSize().heightIn(min = 48.dp)
    if (canGlass) {
        RuntimeCard(modifier = target, containerColor = background, shape = shape, onClick = onClick,
            contentPadding = PaddingValues(0.dp), material = RuntimeSurfaceMaterial.Glass) { slot() }
    } else {
        CupertinoButton(onClick = onClick, enabled = enabled, modifier = target, shape = shape,
            colors = CupertinoButtonDefaults.filledButtonColors(
                containerColor = colors.containerColor, contentColor = colors.contentColor,
                disabledContainerColor = colors.disabledContainerColor, disabledContentColor = colors.disabledContentColor,
            ), border = border, contentPadding = contentPadding) {
            CupertinoContent { content() }
        }
    }
}

@Composable
internal fun ChengmingIconButton(
    onClick: () -> Unit, modifier: Modifier, enabled: Boolean,
    accessibilityLabel: String?, content: @Composable () -> Unit,
) {
    if (LocalCupertinoChrome.current) {
        Box(modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp).semantics {
            accessibilityLabel?.let { contentDescription = it }
        }.clickable(enabled = enabled, role = Role.Button, onClick = onClick), contentAlignment = Alignment.Center) {
            RuntimeCard(modifier = Modifier.size(36.dp), shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface, contentPadding = PaddingValues(0.dp),
                material = RuntimeSurfaceMaterial.Glass) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) { content() }
                }
            }
        }
        return
    }
    val colors = ButtonDefaults.textButtonColors(
        containerColor = if (LocalCupertinoChrome.current) MaterialTheme.colorScheme.surface else Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )
    ChengmingButton(onClick, modifier.semantics {
        accessibilityLabel?.let { contentDescription = it }
    }.sizeIn(minWidth = 48.dp, minHeight = 48.dp), enabled, PaddingValues(10.dp), CircleShape,
        colors, null, glass = LocalCupertinoChrome.current) { content() }
}

@Composable
internal fun ChengmingSwitch(
    checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier,
    enabled: Boolean, colors: SwitchColors, thumbContent: (@Composable () -> Unit)?,
) {
    if (!LocalChengmingAccessibility.current.reduceMotion) {
        CupertinoSwitch(checked = checked, onCheckedChange = onCheckedChange,
            modifier = modifier.minimumInteractiveComponentSize(), enabled = enabled, thumbContent = thumbContent,
            colors = CupertinoSwitchDefaults.colors(checkedTrackColor = colors.checkedTrackColor,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                disabledCheckedTrackColor = colors.disabledCheckedTrackColor,
                disabledUncheckedTrackColor = colors.disabledUncheckedTrackColor))
        return
    }
    val track = when {
        !enabled && checked -> colors.disabledCheckedTrackColor
        !enabled -> colors.disabledUncheckedTrackColor
        checked -> colors.checkedTrackColor
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val highContrast = LocalChengmingAccessibility.current.increaseContrast
    Box(modifier.minimumInteractiveComponentSize().sizeIn(minWidth = 52.dp, minHeight = 48.dp)
        .toggleable(checked, enabled = enabled, role = Role.Switch, onValueChange = onCheckedChange),
        contentAlignment = Alignment.Center) {
        Box(Modifier.size(51.dp, 31.dp).clip(CircleShape).background(track)
            .then(if (highContrast) Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape) else Modifier)) {
            Box(Modifier.align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                .padding(2.dp).size(27.dp).clip(CircleShape)
                .background(if (enabled) Color.White else MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center) { thumbContent?.invoke() }
        }
    }
}

@Composable
internal fun ChengmingSelection(
    selected: Boolean, onClick: (() -> Unit)?, modifier: Modifier,
    enabled: Boolean, radio: Boolean, selectedColor: Color, unselectedColor: Color,
) {
    val shape = CircleShape
    val color = if (selected) selectedColor else unselectedColor
    val foreground = if (color.luminance() > 0.179f) Color.Black else Color.White
    Box(modifier.minimumInteractiveComponentSize().sizeIn(minWidth = 48.dp, minHeight = 48.dp)
        .then(if (onClick == null) Modifier else if (radio) Modifier.selectable(
            selected, enabled = enabled, role = Role.RadioButton, onClick = onClick,
        ) else Modifier.toggleable(selected, enabled = enabled, role = Role.Checkbox) { onClick() }),
        contentAlignment = Alignment.Center) {
        Box(Modifier.size(22.dp).clip(shape)
            .background(if (selected) color else Color.Transparent)
            .border(1.5.dp, color, shape), contentAlignment = Alignment.Center) {
            if (selected) Text("✓", color = foreground, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** Cupertino components expose their own content locals; app slots still use Material Text/Icon. */
@Composable
internal fun CupertinoContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides io.github.alexzhirkevich.LocalContentColor.current,
        LocalTextStyle provides io.github.alexzhirkevich.LocalTextStyle.current,
        content = content,
    )
}

@Composable
internal fun ChengmingCheckbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier, enabled: Boolean, colors: CheckboxColors) {
    CupertinoCheckBox(checked = checked, onCheckedChange = onCheckedChange,
        modifier = modifier.minimumInteractiveComponentSize(), enabled = enabled,
        colors = CupertinoCheckboxDefaults.colors(checkedBoxColor = colors.checkedBoxColor,
            checkedCheckmarkColor = colors.checkedCheckmarkColor, uncheckedBorderColor = colors.uncheckedBorderColor,
            disabledCheckedBoxColor = colors.disabledCheckedBoxColor,
            disabledUncheckedBorderColor = colors.disabledUncheckedBorderColor))
}

@Composable
internal fun ChengmingFilterChip(
    selected: Boolean, onClick: () -> Unit, label: @Composable () -> Unit, modifier: Modifier,
    enabled: Boolean, leadingIcon: (@Composable () -> Unit)?, trailingIcon: (@Composable () -> Unit)?,
    shape: Shape,
    colors: SelectableChipColors,
) {
    // Cupertino has no filter-chip primitive. Retain the supplied color/state contract on a plain surface.
    FilterChip(selected = selected, onClick = onClick, label = label,
        modifier = modifier.minimumInteractiveComponentSize().heightIn(min = 48.dp), enabled = enabled,
        leadingIcon = leadingIcon, trailingIcon = trailingIcon, shape = shape, colors = colors)
}
