package top.wkbin.taixu.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import top.wkbin.taixu.core.datastore.AppearancePreferences
import top.wkbin.taixu.core.model.ChengmingAccessibilityOptions
import top.wkbin.taixu.feature.settings.R
import top.wkbin.taixu.ui.components.RuntimeIconName

@Composable
internal fun ChengmingAccessibilitySettings(preferences: AppearancePreferences = koinInject()) {
    val options by preferences.accessibility.options.collectAsStateWithLifecycle(ChengmingAccessibilityOptions())
    val scope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(stringResource(R.string.chengming_accessibility), style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 4.dp))
        SettingsGroup {
            ToggleRow(RuntimeIconName.Visibility, stringResource(R.string.chengming_reduce_transparency),
                stringResource(R.string.chengming_reduce_transparency_description), options.reduceTransparency,
                { scope.launch { preferences.accessibility.setReduceTransparency(it) } })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            ToggleRow(RuntimeIconName.FontSize, stringResource(R.string.chengming_increase_contrast),
                stringResource(R.string.chengming_increase_contrast_description), options.increaseContrast,
                { scope.launch { preferences.accessibility.setIncreaseContrast(it) } })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            ToggleRow(RuntimeIconName.Vibrate, stringResource(R.string.chengming_reduce_motion),
                stringResource(R.string.chengming_reduce_motion_description), options.reduceMotion,
                { scope.launch { preferences.accessibility.setReduceMotion(it) } })
        }
    }
}
