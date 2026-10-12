@file:OptIn(io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi::class)

package top.wkbin.taixu.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import io.github.alexzhirkevich.cupertino.CupertinoDropdownMenu
import io.github.alexzhirkevich.cupertino.CupertinoMenuScope
import io.github.alexzhirkevich.cupertino.MenuAction

internal val LocalCupertinoMenuScope = staticCompositionLocalOf<CupertinoMenuScope?> { null }

@Composable
internal fun ChengmingDropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit, modifier: Modifier,
    offset: DpOffset, properties: PopupProperties, shape: Shape, content: @Composable ColumnScope.() -> Unit) {
    CupertinoDropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest,
        modifier = modifier.clip(shape), offset = offset, properties = properties,
        containerColor = MaterialTheme.colorScheme.surface) {
        CompositionLocalProvider(LocalCupertinoMenuScope provides this, LocalCupertinoChrome provides false) {
            Column(content = content)
        }
    }
}

@Composable
internal fun ChengmingMenuItem(scope: CupertinoMenuScope, text: @Composable () -> Unit, onClick: () -> Unit,
    modifier: Modifier, leadingIcon: (@Composable () -> Unit)?, trailingIcon: (@Composable () -> Unit)?, enabled: Boolean) {
    with(scope) {
        MenuAction(onClick = onClick, enabled = enabled, modifier = modifier.heightIn(min = 48.dp),
            contentColor = MaterialTheme.colorScheme.onSurface,
            icon = { CupertinoContent { leadingIcon?.invoke() } }) {
            CupertinoContent {
                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.weight(1f)) { text() }; trailingIcon?.invoke()
                }
            }
        }
    }
}
