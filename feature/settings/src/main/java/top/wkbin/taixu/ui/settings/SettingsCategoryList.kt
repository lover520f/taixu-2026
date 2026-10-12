package top.wkbin.taixu.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.ui.components.IconTile
import top.wkbin.taixu.ui.components.RuntimeGroupedList
import top.wkbin.taixu.ui.components.RuntimeIcon
import top.wkbin.taixu.ui.components.RuntimeIconName
import top.wkbin.taixu.ui.settings.LocalizedText as Text

internal data class SettingsCategory(
    val icon: RuntimeIconName,
    val accent: Color,
    val title: String,
    val subtitle: String,
    val badge: String,
    val onClick: () -> Unit,
)

/** Cupertino content rows share one surface; descriptive text grows with font scale. */
@Composable
internal fun SettingsCategoryList(categories: List<SettingsCategory>) {
    RuntimeGroupedList(
        itemCount = categories.size,
        modifier = Modifier.fillMaxWidth(),
        dividerInset = 56.dp,
    ) { index ->
        val category = categories[index]
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable(role = Role.Button, onClick = category.onClick)
                .heightIn(min = 54.dp)
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconTile(icon = category.icon, size = 28.dp, color = category.accent)
            Text(category.title, modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(category.badge, modifier = Modifier.widthIn(max = 108.dp),
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            RuntimeIcon(
                RuntimeIconName.ChevronRight,
                Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
