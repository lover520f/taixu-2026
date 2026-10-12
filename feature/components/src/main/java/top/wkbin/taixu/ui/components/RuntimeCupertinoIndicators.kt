@file:OptIn(io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi::class)

package top.wkbin.taixu.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility
import io.github.alexzhirkevich.cupertino.CupertinoSlider
import io.github.alexzhirkevich.cupertino.CupertinoSliderDefaults
import io.github.alexzhirkevich.cupertino.CupertinoActivityIndicator

@Composable
internal fun ChengmingLinearProgress(progress: (() -> Float)?, modifier: Modifier, color: Color, trackColor: Color) {
    val reduced = LocalChengmingAccessibility.current.reduceMotion
    val fraction = progress?.invoke()?.coerceIn(0f, 1f)
    val phase = if (fraction != null || reduced) 0.5f else {
        val animation = rememberInfiniteTransition(label = "cupertinoProgress")
        val value by animation.animateFloat(0f, 1f,
            infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "progressPhase")
        value
    }
    Canvas(modifier.heightIn(min = 4.dp).semantics {
        progressBarRangeInfo = fraction?.let { ProgressBarRangeInfo(it, 0f..1f) } ?: ProgressBarRangeInfo.Indeterminate
    }) {
        val stroke = 4.dp.toPx()
        drawLine(trackColor, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), stroke, StrokeCap.Round)
        val start = if (fraction == null) phase * size.width * 0.65f else 0f
        val end = if (fraction == null) start + size.width * 0.35f else size.width * fraction
        drawLine(color, Offset(start, size.height / 2), Offset(end, size.height / 2), stroke, StrokeCap.Round)
    }
}

@Composable
internal fun ChengmingActivityIndicator(modifier: Modifier, color: Color, strokeWidth: Dp) {
    if (!LocalChengmingAccessibility.current.reduceMotion) {
        CupertinoActivityIndicator(modifier = modifier.semantics {
            progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        }, color = color, strokeWidth = strokeWidth)
        return
    }
    val reduced = LocalChengmingAccessibility.current.reduceMotion
    val phase = if (reduced) 0 else {
        val animation = rememberInfiniteTransition(label = "cupertinoActivity")
        val value by animation.animateFloat(0f, 12f,
            infiniteRepeatable(tween(1000, easing = LinearEasing)), label = "activityPhase")
        value.toInt()
    }
    Canvas(modifier.sizeIn(minWidth = 24.dp, minHeight = 24.dp).semantics {
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
    }) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension * 0.4f
        repeat(12) { index ->
            val angle = Math.toRadians(index * 30.0)
            val direction = Offset(kotlin.math.cos(angle).toFloat(), kotlin.math.sin(angle).toFloat())
            val alpha = if (reduced) 0.7f else 0.2f + ((index + phase) % 12) / 12f * 0.8f
            drawLine(color.copy(alpha = alpha), center + direction * radius * 0.55f,
                center + direction * radius, strokeWidth.toPx().coerceAtMost(radius * 0.25f), StrokeCap.Round)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChengmingSlider(
    value: Float, onValueChange: (Float) -> Unit, modifier: Modifier, enabled: Boolean,
    valueRange: ClosedFloatingPointRange<Float>, steps: Int, onValueChangeFinished: (() -> Unit)?, colors: SliderColors,
) {
    CupertinoSlider(value = value, onValueChange = onValueChange, modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled, valueRange = valueRange, steps = steps, onValueChangeFinished = onValueChangeFinished,
        colors = CupertinoSliderDefaults.colors(thumbColor = Color.White, activeTrackColor = colors.activeTrackColor,
            inactiveTrackColor = colors.inactiveTrackColor, disabledActiveTrackColor = colors.disabledActiveTrackColor,
            disabledInactiveTrackColor = colors.disabledInactiveTrackColor))
}
