@file:OptIn(io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi::class)

package top.wkbin.taixu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.cupertino.CupertinoTopAppBar
import io.github.alexzhirkevich.cupertino.CupertinoTopAppBarDefaults
import top.wkbin.taixu.feature.components.R
import kotlin.math.roundToInt

/** Cupertino owns compact bar geometry; a measured content title collapses with the page scroll. */
@Composable
internal fun ChengmingTopBar(title: String, onBack: (() -> Unit)?, statusText: String?, modifier: Modifier,
    largeTitle: Boolean, scrollState: RuntimeTopBarScrollState?, actions: @Composable RowScope.() -> Unit) {
    val displayTitle = title.substringAfter(" · ")
    val fraction = if (largeTitle) scrollState?.collapsedFraction ?: 0f else 1f
    Column(modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        Box(Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
        if (!largeTitle || fraction > 0.85f || onBack != null) CompositionLocalProvider(LocalCupertinoChrome provides true) {
            CupertinoTopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0), isTranslucent = false, divider = {},
                colors = CupertinoTopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    if (fraction > 0.85f) {
                        Column {
                            Text(displayTitle, style = MaterialTheme.typography.titleMedium, maxLines = 1,
                                overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface)
                            if (statusText != null) Text(statusText, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                },
                navigationIcon = {
                    if (onBack != null) RuntimeIconButton(onClick = onBack,
                        contentDescription = stringResource(R.string.components_back)) {
                        RuntimeIcon(RuntimeIconName.Back, Modifier.size(22.dp))
                    }
                },
                actions = actions,
            )
        }
        if (largeTitle && fraction < 1f) {
            Row(Modifier.clipToBounds().layout { measurable, constraints ->
                val measured = measurable.measure(constraints)
                layout(measured.width, (measured.height * (1f - fraction)).roundToInt()) { measured.placeRelative(0, 0) }
            }.graphicsLayer { alpha = 1f - fraction }.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(displayTitle, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (statusText != null) Text(statusText, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                if (onBack == null && fraction <= 0.85f) CompositionLocalProvider(LocalCupertinoChrome provides true) {
                    Row(content = actions)
                }
            }
        }
        }
    }
}
