package top.wkbin.taixu.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.feature.home.R
import top.wkbin.taixu.ui.components.*

@Composable
internal fun ChengmingResourceSummary(memory: String, memoryDetail: String, memoryProgress: Float,
    storage: String, storageDetail: String, storageProgress: Float) {
    RuntimeGroupedList(2, Modifier.fillMaxWidth()) { index ->
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RuntimeIcon(if (index == 0) RuntimeIconName.Cpu else RuntimeIconName.Storage,
                    Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Text(stringResource(if (index == 0) R.string.home_memory else R.string.home_storage),
                    Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                Text(if (index == 0) memory else storage, style = MaterialTheme.typography.bodyMedium)
            }
            RuntimeLinearProgressIndicator(progress = { if (index == 0) memoryProgress else storageProgress },
                modifier = Modifier.fillMaxWidth().height(3.dp))
            Text(if (index == 0) memoryDetail else storageDetail, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
