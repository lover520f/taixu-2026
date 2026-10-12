@file:OptIn(io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi::class)

package top.wkbin.taixu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility
import top.wkbin.taixu.ui.theme.LocalLiquidGlassBackdrop
import top.wkbin.taixu.ui.theme.LocalLiquidGlassSurfaceBackdrop
import io.github.alexzhirkevich.cupertino.CupertinoSegmentedControl
import io.github.alexzhirkevich.cupertino.CupertinoSegmentedControlTab

@Composable
internal fun ChengmingBottomBar(selected: MainDestination, onNavigate: (MainDestination) -> Unit, modifier: Modifier) {
    val glass = LocalChengmingAccessibility.current.glassEnabled
    Box(modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center) {
        CompositionLocalProvider(LocalLiquidGlassSurfaceBackdrop provides LocalLiquidGlassBackdrop.current) {
            RuntimeCard(modifier = Modifier.widthIn(max = 560.dp).fillMaxWidth(), shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                material = if (glass) RuntimeSurfaceMaterial.Glass else RuntimeSurfaceMaterial.Content) {
                Row(Modifier.fillMaxWidth().selectableGroup(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    MainDestination.entries.forEach { destination ->
                        val active = destination == selected
                        val foreground = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        Column(Modifier.weight(1f).clip(RoundedCornerShape(22.dp))
                            .background(if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else androidx.compose.ui.graphics.Color.Transparent)
                            .selectable(active, role = Role.Tab, onClick = { onNavigate(destination) })
                            .heightIn(min = 48.dp).padding(horizontal = 4.dp, vertical = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(chengmingTabSymbol(destination, active), null, Modifier.size(23.dp), tint = foreground)
                            Text(stringResource(destination.labelRes), style = MaterialTheme.typography.labelSmall,
                                color = foreground, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun <T> ChengmingSegmentedControl(
    items: List<T>, selectedIndex: Int, onSelect: (Int) -> Unit, modifier: Modifier, enabled: Boolean,
    itemContent: @Composable (T, Boolean) -> Unit,
) {
    if (items.isEmpty()) return
    if (!LocalChengmingAccessibility.current.reduceMotion) {
        CupertinoSegmentedControl(selectedTabIndex = selectedIndex.coerceIn(items.indices), modifier = modifier) {
            items.forEachIndexed { index, item ->
                CupertinoSegmentedControlTab(onClick = { if (enabled) onSelect(index) },
                    isSelected = index == selectedIndex, modifier = Modifier.heightIn(min = 48.dp)) {
                    CupertinoContent { itemContent(item, index == selectedIndex) }
                }
            }
        }
        return
    }
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val itemWidth = (maxWidth / items.size).coerceAtLeast(72.dp)
        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            Row(Modifier.horizontalScroll(rememberScrollState()).selectableGroup().padding(3.dp)) {
                items.forEachIndexed { index, item ->
                    val selected = index == selectedIndex
                    Surface(modifier = Modifier.width(itemWidth - 6.dp / items.size).heightIn(min = 48.dp)
                        .selectable(selected, enabled = enabled, role = Role.Tab, onClick = { onSelect(index) }),
                        shape = RoundedCornerShape(9.dp),
                        color = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant) {
                        Box(Modifier.padding(horizontal = 8.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
                            ProvideTextStyle(MaterialTheme.typography.labelLarge) { itemContent(item, selected) }
                        }
                    }
                }
            }
        }
    }
}
