package top.wkbin.taixu.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.LocalIndication
import androidx.compose.ui.window.PopupProperties
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import top.wkbin.taixu.ui.components.GlassEffects.liquidGlassSurface
import top.wkbin.taixu.feature.components.R
import androidx.compose.ui.res.stringResource
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ButtonDefaults
import top.wkbin.taixu.ui.theme.ThemeStyle
import top.wkbin.taixu.ui.theme.LocalLiquidGlassSurfaceBackdrop
import kotlin.math.abs

/** 全圆角药丸形状 (Capsule Shape) */
val CapsuleShape: Shape = RoundedCornerShape(percent = 50)

// ═══════════════════════════════════════════════════════════════════════════
// 1. RuntimeSegmentedControl（iOS 药丸分段选择器）
// ═══════════════════════════════════════════════════════════════════════════

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun <T> RuntimeSegmentedControl(
    items: List<T>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        ChengmingSegmentedControl(items, selectedIndex, onSelect, modifier, enabled, itemContent)
        return
    }

    if (items.isEmpty()) return

    val haptic = LocalHapticFeedback.current

    // 玄同主题：原生 Material 3 药丸分段条降级
    Surface(
        modifier = modifier
            .clip(CapsuleShape)
            .height(38.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        shape = CapsuleShape,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            val totalWidth = constraints.maxWidth.toFloat()
            val segmentWidth = totalWidth / items.size
            val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr

            val animatedIndex by animateFloatAsState(
                targetValue = selectedIndex.toFloat().coerceIn(0f, (items.size - 1).toFloat()),
                animationSpec = LiquidSpringSpec,
                label = "M3SegmentedOffset",
            )

            // M3 实体滑块
            Surface(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = if (isLtr) animatedIndex * segmentWidth else -animatedIndex * segmentWidth
                    }
                    .width(with(LocalDensity.current) { segmentWidth.toDp() })
                    .height(32.dp),
                shape = CapsuleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
            ) {}

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = index == selectedIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(CapsuleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                enabled = enabled,
                                role = Role.Tab,
                            ) {
                                if (index != selectedIndex) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onSelect(index)
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        itemContent(item, isSelected)
                    }
                }
            }
        }
    }

}

/**
 * 字符型选项分段选择器重载（最常见的文本分段）。
 */
@Composable
fun RuntimeSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    RuntimeSegmentedControl(
        items = options,
        selectedIndex = selectedIndex,
        onSelect = onSelect,
        modifier = modifier,
        enabled = enabled,
    ) { text, isSelected ->
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
            ),
            color = if (isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
            },
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 2. RuntimeOutlinedTextField & RuntimeTextField（Cupertino 文本输入）
// ═══════════════════════════════════════════════════════════════════════════

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = RoundedCornerShape(14.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        ChengmingTextField(value, onValueChange, modifier, enabled, readOnly, textStyle, label, placeholder,
            leadingIcon, trailingIcon, prefix, suffix, supportingText, isError, visualTransformation,
            keyboardOptions, keyboardActions, singleLine, maxLines, minLines, interactionSource, shape, colors)
    } else OutlinedTextField(value, onValueChange, modifier, enabled, readOnly, textStyle, label, placeholder,
        leadingIcon, trailingIcon, prefix, suffix, supportingText, isError, visualTransformation,
        keyboardOptions, keyboardActions, singleLine, maxLines, minLines, interactionSource, shape, colors)
}

/**
 * 填充型文本输入框别名，在液态玻璃下与 OutlinedTextField 共享统一高品质质感。
 */
@Composable
fun RuntimeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = RoundedCornerShape(14.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    RuntimeOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

// ═══════════════════════════════════════════════════════════════════════════
// 3. RuntimeModalBottomSheet（普通内容模态抽屉）
// ═══════════════════════════════════════════════════════════════════════════

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuntimeModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    containerColor: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingBottomSheet(onDismissRequest, modifier, sheetState, shape, containerColor, contentColor,
            tonalElevation, scrimColor, dragHandle, content)
    else ModalBottomSheet(onDismissRequest, modifier, sheetState = sheetState, shape = shape,
        containerColor = if (containerColor != Color.Unspecified) containerColor else MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = contentColor, tonalElevation = tonalElevation, scrimColor = scrimColor,
        dragHandle = dragHandle ?: { BottomSheetDefaults.DragHandle() }, content = content)
}

/** 默认 Apple 质感药丸拖拽手柄 */
@Composable
private fun DefaultGlassDragHandle(isDark: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 36.dp, height = 5.dp)
                .clip(CapsuleShape)
                .background(
                    if (isDark) Color.White.copy(alpha = 0.24f)
                    else Color.Black.copy(alpha = 0.16f),
                ),
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 4. RuntimeDropdownMenu & RuntimeDropdownMenuItem（Cupertino 上下文菜单）
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 太墟 · iOS 规范液态玻璃悬浮上下文菜单 (RuntimeDropdownMenu)。
 *
 * 在澄明主题下：
 * - 采用 Regular 材质，18dp 平滑圆角，带有 16dp 柔和环境光散射阴影；
 * - 窗体顶部带有镜面高光，内部子项提供 menuBackdrop 隔离；
 * 在玄同主题下：
 * - 降级为原生 M3 DropdownMenu。
 */
@Composable
fun RuntimeDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS)
        ChengmingDropdownMenu(expanded, onDismissRequest, modifier, offset, properties, shape, content)
    else DropdownMenu(expanded, onDismissRequest, modifier, offset, properties = properties, shape = shape, content = content)
}

/**
 * 太墟 · iOS 规范液态玻璃上下文菜单项 (RuntimeDropdownMenuItem)。
 */
@Composable
fun RuntimeDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: MenuItemColors = MenuDefaults.itemColors(),
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {
    val scope = LocalCupertinoMenuScope.current
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS && scope != null)
        ChengmingMenuItem(scope, text, onClick, modifier, leadingIcon, trailingIcon, enabled)
    else DropdownMenuItem(text, onClick, modifier, leadingIcon, trailingIcon, enabled,
        colors, contentPadding, interactionSource)
}

// ═══════════════════════════════════════════════════════════════════════════
// 5. RuntimeSearchBar（iOS 胶囊透镜搜索栏）
// ═══════════════════════════════════════════════════════════════════════════

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    leadingIcon: (@Composable () -> Unit)? = null,
    onSearch: ((String) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    RuntimeOutlinedTextField(value = query, onValueChange = onQueryChange, modifier = modifier,
        enabled = enabled, singleLine = true, placeholder = { Text(placeholder) },
        leadingIcon = leadingIcon ?: { RuntimeIcon(RuntimeIconName.Search, Modifier.size(20.dp)) },
        trailingIcon = if (query.isEmpty()) null else {
            { RuntimeIconButton(onClick = { onQueryChange(""); onClear?.invoke() }, enabled = enabled,
                contentDescription = stringResource(R.string.components_clear_search)) {
                RuntimeIcon(RuntimeIconName.Close, Modifier.size(18.dp))
            } }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke(query) }))
}

// ═══════════════════════════════════════════════════════════════════════════
// 6. RuntimeBadge（iOS 色散水滴角标）
// ═══════════════════════════════════════════════════════════════════════════

/** Theme-aware Runtime adapter with Cupertino content and explicit navigation glass. */
@Composable
fun RuntimeBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    val fill = containerColor.copy(alpha = 1f)
    Surface(modifier = modifier, color = fill, contentColor = contentColor, shape = CircleShape) {
        Row(Modifier.padding(horizontal = if (content == null) 3.dp else 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if (content != null) ProvideTextStyle(MaterialTheme.typography.labelSmall) { content() }
            else Spacer(Modifier.size(2.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// 7. RuntimeLiquidButton（iOS 规范纯正液态胶囊按钮）
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 太墟 · iOS 规范液态玻璃胶囊按钮 (RuntimeLiquidButton)。
 *
 * 严格遵从 Kyant Catalog 原版 LiquidButton 物理架构：
 * - 动态漫射高光：内置 AGSL InteractiveHighlight，手指触控位置动态漫射流光；
 * - 速度阻尼拉伸形变：按下与拖拽时根据角度与位移进行 tanh 双轴弹性拉伸；
 * - 光学配置：vibrancy() + blur(2dp) + lens(12dp, 24dp)；
 * - 颜色浸润：支持 BlendMode.Hue 真实色相浸润与 surfaceColor 叠加；
 * - 字体排版：默认使用 AppleTypography.Button（16sp / SemiBold / Tracking -0.2sp）；
 * - 双主题降级：玄同主题下自适应为原生 Material 3 填充/胶囊按钮。
 */
@Composable
fun RuntimeLiquidButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = Color.Unspecified,
    surfaceColor: Color = Color.Unspecified,
    shape: Shape = CapsuleShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        val fill = if (tint.isSpecified) tint else if (surfaceColor.isSpecified) surfaceColor else MaterialTheme.colorScheme.surface
        val foreground = if (fill.luminance() > 0.179f) Color.Black else Color.White
        ChengmingButton(onClick, modifier, enabled, contentPadding, shape,
            ButtonDefaults.buttonColors(containerColor = fill, contentColor = foreground), null,
            glass = true, content = content)
    } else RuntimeButton(onClick, modifier, enabled, shape = shape, contentPadding = contentPadding, content = content)
}

// ═══════════════════════════════════════════════════════════════════════════
// 8. RuntimeSheetHeader（iOS 规范底部抽屉标准顶栏）
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 太墟 · iOS 规范底部抽屉标准顶栏 (RuntimeSheetHeader)。
 *
 * 遵循 iOS UISheetPresentationController 顶栏规范：
 * - 左侧：取消/关闭动作文本；
 * - 中央：标题文本（加粗 Headline 居中）；
 * - 右侧：完成/确认动作高光文本；
 * - 下边缘：极细微高光反光分割线。
 */
@Composable
fun RuntimeSheetHeader(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: (() -> Unit)? = null,
    confirmText: String = "完成",
    dismissText: String = "取消",
) {
    val isDark = MaterialTheme.colorScheme.onSurface.luminance() > 0.5f

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // 左侧取消
            Text(
                text = dismissText,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(CapsuleShape)
                    .clickable(role = Role.Button, onClick = onDismiss)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )

            // 中央标题
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            // 右侧确认/完成
            if (onConfirm != null) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(CapsuleShape)
                        .clickable(role = Role.Button, onClick = onConfirm)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            } else {
                Spacer(Modifier.width(48.dp))
            }
        }

        // 底部 1px 细微光影分割线
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            if (isDark) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.08f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
    }
}
