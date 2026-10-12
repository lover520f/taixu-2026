package top.wkbin.taixu.ui.components
import io.github.alexzhirkevich.cupertino.theme.systemGreen
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.layout.layout
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import androidx.compose.ui.graphics.drawscope.scale
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import top.wkbin.taixu.feature.components.R
import androidx.compose.ui.res.stringResource
import top.wkbin.taixu.ui.theme.LocalLiquidGlassBackdrop
import top.wkbin.taixu.ui.theme.LocalLiquidGlassSurfaceBackdrop
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import top.wkbin.taixu.ui.theme.ThemeStyle
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility

/**
 * 太墟 (TaiXu) 四大核心中枢导航定义：
 * 太墟（开辟画布）· 智枢（AI 结对）· 工坊（工作区）· 乾坤（设置与模型）
 */
enum class MainDestination(val labelRes: Int, val subtitleRes: Int, val icon: RuntimeIconName) {
    Home(R.string.components_nav_home, R.string.components_nav_subtitle_home, RuntimeIconName.NavDashboard),
    Agent(R.string.components_nav_agent, R.string.components_nav_subtitle_agent, RuntimeIconName.NavMessage),
    Workspace(R.string.components_nav_workspace, R.string.components_nav_subtitle_workspace, RuntimeIconName.NavRepository),
    Settings(R.string.components_nav_settings, R.string.components_nav_subtitle_settings, RuntimeIconName.NavSettings),
}

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeBottomBar(
    selected: MainDestination,
    onNavigate: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) ChengmingBottomBar(selected, onNavigate, modifier)
    else StandardBottomBar(selected, onNavigate, modifier)
}

@Composable
private fun StandardBottomBar(
    selected: MainDestination,
    onNavigate: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 3.dp,
    ) {
        MainDestination.entries.forEach { destination ->
            val isSelected = destination == selected
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onNavigate(destination)
                },
                icon = { RuntimeIcon(destination.icon, Modifier.size(24.dp)) },
                label = {
                    Text(
                        text = stringResource(destination.labelRes),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        ),
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}



/** Marks a page-content layer as the safe source for liquid-glass consumers. */
@Composable
fun Modifier.liquidGlassContent(): Modifier {
    val backdrop = LocalLiquidGlassBackdrop.current
    return if (backdrop != null) layerBackdrop(backdrop) else this
}

/** Returns whether the current composition is hosted by the Chengming glass theme. */
@Composable
fun isLiquidGlassThemeActive(): Boolean = LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS

/**
 * 太墟品牌 TopBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuntimeTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    statusText: String? = null,
    modifier: Modifier = Modifier,
    largeTitle: Boolean = false,
    scrollState: RuntimeTopBarScrollState? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        ChengmingTopBar(title, onBack, statusText, modifier, largeTitle, scrollState, actions)
        return
    }
    val topBar: @Composable () -> Unit = {
        TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (onBack == null) TaiXuBrandBadge(30.dp)
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (statusText != null) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (onBack != null) {
                RuntimeIconButton(onClick = onBack, contentDescription = stringResource(R.string.components_back)) {
                    RuntimeIcon(RuntimeIconName.Back, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        )
    }
    Box(modifier = modifier.fillMaxWidth()) { topBar() }
}

/**
 * 太墟品牌标志徽章
 */
@Composable
fun TaiXuBrandBadge(size: Dp = 38.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.22f))
            .background(Color.White)
            .padding(size * 0.10f),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.components_taixu_logo),
            contentDescription = stringResource(R.string.components_taixu_logo),
            modifier = Modifier.size(size * 0.80f),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(Modifier.weight(1f, fill = false), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                title,
                style = if (isLiquidGlassThemeActive()) MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant) else MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        trailing?.invoke()
    }
}

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tonal: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
    shape: Shape = RoundedCornerShape(24.dp),
    colors: ButtonColors? = null,
    border: BorderStroke? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val resolved = colors ?: if (tonal) ButtonDefaults.filledTonalButtonColors() else ButtonDefaults.buttonColors()
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        ChengmingButton(onClick, modifier, enabled, contentPadding, shape, resolved, border,
            glass = LocalCupertinoChrome.current, content = content)
    } else if (tonal) FilledTonalButton(onClick, modifier, enabled, shape = shape, colors = resolved,
        border = border, contentPadding = contentPadding, content = content)
    else Button(onClick, modifier, enabled, shape = shape, colors = resolved,
        border = border, contentPadding = contentPadding, content = content)
}

/** Theme-dispatched switch with a static reduced-motion variant. */
@Composable
fun RuntimeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchColors = if (isLiquidGlassThemeActive()) SwitchDefaults.colors(checkedTrackColor = io.github.alexzhirkevich.cupertino.theme.CupertinoColors.systemGreen, disabledCheckedTrackColor = io.github.alexzhirkevich.cupertino.theme.CupertinoColors.systemGreen.copy(alpha = 0.33f), checkedThumbColor = Color.White) else SwitchDefaults.colors(),
    thumbContent: (@Composable (() -> Unit))? = null,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingSwitch(checked, onCheckedChange, modifier, enabled, colors, thumbContent)
    else androidx.compose.material3.Switch(checked, onCheckedChange, modifier, enabled = enabled,
        colors = colors, thumbContent = thumbContent)
}

@Composable
fun RuntimeOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(24.dp),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingButton(onClick, modifier, enabled, contentPadding, shape, colors, border, content = content)
    else androidx.compose.material3.OutlinedButton(onClick, modifier, enabled, shape = shape,
        colors = colors, border = border, contentPadding = contentPadding, content = content)
}

@Composable
fun RuntimeFilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(24.dp),
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit,
) = RuntimeButton(onClick, modifier, enabled, tonal = true, contentPadding = contentPadding, shape = shape, colors = colors, border = border, content = content)

@Composable
fun RuntimeTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable RowScope.() -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingButton(onClick, modifier, enabled, contentPadding, shape, colors, null, content = content)
    else androidx.compose.material3.TextButton(onClick, modifier, enabled, shape = shape,
        colors = colors, contentPadding = contentPadding, content = content)
}

@Composable
fun RuntimeIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingIconButton(onClick, modifier, enabled, contentDescription, content)
    else IconButton(onClick, modifier.semantics { contentDescription?.let { this.contentDescription = it } },
        enabled, content = content)
}

/** Semantic linear progress; Chengming uses a plain track rather than a glass tube. */
@Composable
fun RuntimeLinearProgressIndicator(
    progress: (() -> Float)? = null,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingLinearProgress(progress, modifier, color, trackColor)
    else if (progress == null) androidx.compose.material3.LinearProgressIndicator(modifier, color, trackColor)
    else androidx.compose.material3.LinearProgressIndicator(progress, modifier, color, trackColor)
}

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = Color.Transparent,
    strokeWidth: Dp = 4.5.dp,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingActivityIndicator(modifier, color, strokeWidth)
    else androidx.compose.material3.CircularProgressIndicator(modifier, color, strokeWidth, trackColor)
}

@Composable
fun RuntimeCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingCheckbox(checked, onCheckedChange, modifier, enabled, colors)
    else androidx.compose.material3.Checkbox(checked, onCheckedChange, modifier, enabled, colors = colors)
}

@Composable
fun RuntimeRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingSelection(selected, onClick, modifier, enabled, true,
            if (enabled) colors.selectedColor else colors.disabledSelectedColor,
            if (enabled) colors.unselectedColor else colors.disabledUnselectedColor)
    else androidx.compose.material3.RadioButton(selected, onClick, modifier, enabled, colors = colors)
}

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = if (isLiquidGlassThemeActive()) SliderDefaults.colors(thumbColor = Color.White) else SliderDefaults.colors(),
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingSlider(value, onValueChange, modifier, enabled, valueRange, steps, onValueChangeFinished, colors)
    else androidx.compose.material3.Slider(value, onValueChange, modifier, enabled,
        valueRange = valueRange, steps = steps, onValueChangeFinished = onValueChangeFinished, colors = colors)
}

/** Theme-dispatched alert with a scroll-safe Cupertino form body in Chengming. */
@Composable
fun RuntimeAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingAlertDialog(onDismissRequest, confirmButton, modifier, dismissButton, title, text, icon)
    else AlertDialog(onDismissRequest, confirmButton, modifier, dismissButton = dismissButton,
        title = title, text = text, icon = icon)
}


/**
 * 呼吸状态指示药丸
 */
@Composable
fun StatusBadge(
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    pulsing: Boolean = false,
) {
    val alphaAnim = if (pulsing && !LocalChengmingAccessibility.current.reduceMotion) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "pulseAlpha",
        ).value
    } else 1f

    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.12f),
        shape = CircleShape,
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
    ) {
        Row(
            Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = alphaAnim)),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = color,
                maxLines = 1,
                softWrap = false,
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isCode: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.38f, fill = false),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            modifier = Modifier.weight(0.62f, fill = false),
        )
    }
}

@Composable
fun CodeBlockRow(
    label: String,
    code: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val backdrop = LocalLiquidGlassSurfaceBackdrop.current
        val shape = RoundedCornerShape(8.dp)
        val codeModifier = if (backdrop == null) {
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        } else {
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { shape },
                    effects = {
                        vibrancy()
                        blur(3.dp.toPx())
                        lens(4.dp.toPx(), 6.dp.toPx(), depthEffect = true)
                    },
                    highlight = { Highlight.Default.copy(alpha = 0.20f) },
                    onDrawSurface = { drawRoundRect(Color.Black.copy(alpha = 0.20f)) },
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        }
        Box(modifier = codeModifier) {
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                ),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun EmptyPanel(
    icon: RuntimeIconName,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    RuntimeCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        borderColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconTile(icon, color = MaterialTheme.colorScheme.primary, size = 48.dp)
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
fun NoticeBanner(
    text: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onDismiss: (() -> Unit)? = null,
) {
    val backdrop = LocalLiquidGlassSurfaceBackdrop.current
    val color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val shape = RoundedCornerShape(12.dp)
    val bannerModifier = if (backdrop == null) {
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.25f), shape)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    } else {
        modifier
            .fillMaxWidth()
            .clip(shape)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(4.dp.toPx())
                    lens(6.dp.toPx(), 10.dp.toPx(), depthEffect = true)
                },
                highlight = { Highlight.Default.copy(alpha = 0.25f) },
                shadow = { Shadow(radius = 4.dp, alpha = 0.08f) },
                onDrawSurface = { drawRoundRect(color.copy(alpha = 0.12f)) },
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    }
    Row(
        modifier = bannerModifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RuntimeIcon(if (isError) RuntimeIconName.Alert else RuntimeIconName.Check, Modifier.size(17.dp), color)
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (onDismiss != null) {
            val dismissLabel = stringResource(R.string.components_close)
            RuntimeIconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp),
                contentDescription = dismissLabel,
            ) {
                RuntimeIcon(RuntimeIconName.Close, Modifier.size(14.dp), MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * 🌟 滚动列表平滑渐隐遮罩 (Scroll Fading Edge Mask)
 * 为列表顶部和底部提供平滑的 Alpha 渐隐过渡，消除与 TopBar / BottomBar 之间的生硬切边。
 *
 * ⚠️ 性能提示（2026-09-14）：本实现依赖 `CompositingStrategy.Offscreen` ——
 * `BlendMode.DstIn` 要求先渲染到独立离屏缓冲才能做遮罩。挂在 LazyColumn 上时，
 * **滚动期间每帧都要全量离屏合成**，屏幕越大代价越高，表现为「静止不卡、快速滑动明显掉帧」。
 * 消息列表已改用 [ScrollFadeOverlay]（列表外叠加渐变，代价近乎为零）。
 * 仅在「非滚动、或面积很小」的场景继续使用本修饰符。
 */
fun Modifier.scrollFadingEdge(
    top: Dp = 14.dp,
    bottom: Dp = 14.dp,
): Modifier = this.graphicsLayer {
    compositingStrategy = CompositingStrategy.Offscreen
}.drawWithContent {
    drawContent()
    val topPx = top.toPx()
    val bottomPx = bottom.toPx()
    val h = size.height

    if (topPx > 0f && h > 0f) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startY = 0f,
                endY = topPx.coerceAtMost(h),
            ),
            blendMode = BlendMode.DstIn,
        )
    }
    if (bottomPx > 0f && h > 0f) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startY = (h - bottomPx).coerceAtLeast(0f),
                endY = h,
            ),
            blendMode = BlendMode.DstIn,
        )
    }
}

/**
 * 🌟 滚动渐隐遮罩（轻量版）：绘制为**叠加在滚动内容之上**的两段渐变，不使用离屏合成。
 *
 * 与 [scrollFadingEdge] 的取舍：
 *  - scrollFadingEdge 用 DstIn 真正「擦除」内容 alpha（效果更干净），
 *    但要求 Offscreen 合成 → 滚动每帧全量离屏渲染，是滑动卡顿的头号来源。
 *  - 本实现改为「在内容上方叠一层从背景色到透明的渐变」，视觉近似（顶/底自然淡出），
 *    但**零离屏合成开销**，适合挂在长列表这类滚动频繁、面积大的场景。
 *
 * 用法：把滚动内容与本 Overlay 放进同一个 Box，Overlay 位于内容之上：
 * ```
 * Box {
 *     LazyColumn(...)
 *     ScrollFadeOverlay(top = 4.dp, bottom = 20.dp)
 * }
 * ```
 */
@Composable
fun ScrollFadeOverlay(
    modifier: Modifier = Modifier,
    top: Dp = 14.dp,
    bottom: Dp = 14.dp,
    color: Color = MaterialTheme.colorScheme.surface,
) {
    Box(modifier.fillMaxSize()) {
        if (top > 0.dp) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(top)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(listOf(color, color.copy(alpha = 0f))),
                    ),
            )
        }
        if (bottom > 0.dp) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(bottom)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(listOf(color.copy(alpha = 0f), color)),
                    ),
            )
        }
    }
}

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 3.dp,
        pressedElevation = 6.dp,
    ),
    content: @Composable () -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        CompositionLocalProvider(LocalCupertinoChrome provides true) {
            ChengmingButton(onClick, modifier.sizeIn(minWidth = 56.dp, minHeight = 56.dp), true,
                PaddingValues(16.dp), shape,
                ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor), null,
                glass = true) { content() }
        }
    } else FloatingActionButton(onClick, modifier, shape, containerColor, contentColor, elevation, content = content)
}

/**
 * 主题自适应小型悬浮操作按钮 (M3 Small Floating Action Button)：
 * 遵循 M3 规范：尺寸 40dp，形状 RoundedCornerShape(12.dp)。
 */
@Composable
fun RuntimeSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 3.dp,
        pressedElevation = 6.dp,
    ),
    content: @Composable () -> Unit,
) {
    RuntimeFloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(40.dp),
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        content = content,
    )
}

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingFilterChip(selected, onClick, label, modifier, enabled, leadingIcon, trailingIcon, shape, colors)
    else FilterChip(selected, onClick, label, modifier, enabled, leadingIcon = leadingIcon,
        trailingIcon = trailingIcon, shape = shape, colors = colors)
}

