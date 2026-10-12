package top.wkbin.taixu.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.ui.components.*

internal data class ChengmingQuickAction(val icon: RuntimeIconName, val title: String,
    val summary: String, val onClick: () -> Unit)

@Composable
internal fun ChengmingQuickActions(actions: List<ChengmingQuickAction>) {
    RuntimeGroupedList(actions.size, Modifier.fillMaxWidth(), dividerInset = 48.dp) { index ->
        val action = actions[index]
        Row(Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = action.onClick)
            .heightIn(min = 58.dp).padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RuntimeIcon(action.icon, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(action.title, style = MaterialTheme.typography.bodyMedium, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurface)
                Text(action.summary, style = MaterialTheme.typography.bodySmall, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RuntimeIcon(RuntimeIconName.ChevronRight, Modifier.size(12.dp), tint = MaterialTheme.colorScheme.outline)
        }
    }
}
