package top.wkbin.taixu.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import top.wkbin.taixu.ui.theme.ThemeStyle

/** Scroll the body, retain reachable actions, and use the available window/IME height. */
@Composable
internal fun ChengmingAlertDialog(
    onDismissRequest: () -> Unit, confirmButton: @Composable () -> Unit, modifier: Modifier,
    dismissButton: (@Composable () -> Unit)?, title: (@Composable () -> Unit)?,
    text: (@Composable () -> Unit)?, icon: (@Composable () -> Unit)?,
) {
    val hairline = (1f / LocalDensity.current.density).dp
    Dialog(onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BoxWithConstraints(Modifier.fillMaxSize().imePadding().safeDrawingPadding(), contentAlignment = Alignment.Center) {
            Surface(modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                .widthIn(max = 320.dp).fillMaxWidth().heightIn(max = (maxHeight - 32.dp).coerceAtLeast(48.dp)),
                shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                border = if (LocalChengmingAccessibility.current.increaseContrast) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null) {
                Column {
                    Column(Modifier.weight(1f, fill = false).padding(horizontal = 16.dp, vertical = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        icon?.invoke()
                        if (title != null) ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)) { title() }
                        if (text != null) ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)) { text() }
                    }
                    HorizontalDivider(thickness = hairline, color = MaterialTheme.colorScheme.outlineVariant)
                    if (dismissButton != null) {
                        Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) { dismissButton() }
                            Box(Modifier.width(hairline).fillMaxHeight().background(MaterialTheme.colorScheme.outlineVariant))
                            Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) { confirmButton() }
                        }
                    } else {
                        Box(Modifier.fillMaxWidth().height(48.dp), contentAlignment = Alignment.Center) { confirmButton() }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChengmingBottomSheet(
    onDismissRequest: () -> Unit, modifier: Modifier, sheetState: SheetState, shape: Shape,
    containerColor: androidx.compose.ui.graphics.Color, contentColor: androidx.compose.ui.graphics.Color,
    tonalElevation: androidx.compose.ui.unit.Dp, scrimColor: androidx.compose.ui.graphics.Color,
    dragHandle: (@Composable () -> Unit)?, content: @Composable ColumnScope.() -> Unit,
) {
    val cupertinoShape = if (shape == RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    } else shape
    ModalBottomSheet(
        onDismissRequest = onDismissRequest, modifier = modifier, sheetState = sheetState,
        shape = cupertinoShape, containerColor = if (containerColor == androidx.compose.ui.graphics.Color.Unspecified)
            MaterialTheme.colorScheme.surface else containerColor.copy(alpha = 1f),
        contentColor = contentColor, tonalElevation = tonalElevation, scrimColor = scrimColor,
        dragHandle = dragHandle ?: {
            Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                Box(
                    Modifier.size(width = 36.dp, height = 5.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                )
            }
        },
    ) {
        CompositionLocalProvider(LocalCupertinoChrome provides false) { content() }
    }
}

/** 跨主题动作表单项 (iOS ActionSheet / Material BottomSheet Action) */
data class RuntimeActionSheetItem(
    val title: String,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit,
)

/** Theme-dispatched ActionSheet: authentic floating iOS cards in Chengming, Material Sheet in Xuantong. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuntimeActionSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    items: List<RuntimeActionSheetItem>,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    cancelText: String = "取消",
    onCancel: () -> Unit = onDismissRequest,
) {
    if (!visible) return
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS) {
        ChengmingActionSheet(onDismissRequest, items, modifier, title, message, cancelText, onCancel)
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                if (title != null || message != null) {
                    Column(
                        Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        if (title != null) Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        if (message != null) Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
                items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { item.onClick(); onDismissRequest() }.padding(horizontal = 24.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (item.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (item.isDestructive) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onCancel).padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(text = cancelText, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
internal fun ChengmingActionSheet(
    onDismissRequest: () -> Unit,
    items: List<RuntimeActionSheetItem>,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    cancelText: String = "取消",
    onCancel: () -> Unit = onDismissRequest,
) {
    val hairline = (1f / LocalDensity.current.density).dp
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier.fillMaxSize().clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null, onClick = onDismissRequest,
            ).navigationBarsPadding().padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = modifier.fillMaxWidth().clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null, onClick = {},
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface,
                    border = if (LocalChengmingAccessibility.current.increaseContrast)
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        if (title != null || message != null) {
                            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (title != null) Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center))
                                if (message != null) Text(message, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center))
                            }
                            HorizontalDivider(thickness = hairline, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                        items.forEachIndexed { index, item ->
                            val color = if (item.isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            Box(
                                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).clickable { item.onClick(); onDismissRequest() },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(item.title, style = MaterialTheme.typography.bodyLarge.copy(color = color, fontWeight = FontWeight.Normal))
                            }
                            if (index < items.size - 1) {
                                HorizontalDivider(thickness = hairline, color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxWidth().height(54.dp).clickable(onClick = onCancel),
                    shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    border = if (LocalChengmingAccessibility.current.increaseContrast)
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(cancelText, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                    }
                }
            }
        }
    }
}
