package top.wkbin.taixu.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import top.wkbin.taixu.core.model.ChengmingAccessibilityOptions

/** Appearance schema lives in the datastore domain and is shared by UI consumers. */
internal object ChengmingAccessibilityKeys {
    val ReduceTransparency = booleanPreferencesKey("chengming_reduce_transparency")
    val IncreaseContrast = booleanPreferencesKey("chengming_increase_contrast")
    val ReduceMotion = booleanPreferencesKey("chengming_reduce_motion")
}

class ChengmingAccessibilityPreferences internal constructor(store: SettingsDataStore) {
    private val dataStore = store.appearanceStore
    val options = dataStore.data.map { preferences ->
        ChengmingAccessibilityOptions(
            reduceTransparency = preferences[ChengmingAccessibilityKeys.ReduceTransparency] ?: false,
            increaseContrast = preferences[ChengmingAccessibilityKeys.IncreaseContrast] ?: false,
            reduceMotion = preferences[ChengmingAccessibilityKeys.ReduceMotion] ?: false,
        )
    }

    suspend fun setOptions(options: ChengmingAccessibilityOptions) {
        dataStore.edit {
            it[ChengmingAccessibilityKeys.ReduceTransparency] = options.reduceTransparency
            it[ChengmingAccessibilityKeys.IncreaseContrast] = options.increaseContrast
            it[ChengmingAccessibilityKeys.ReduceMotion] = options.reduceMotion
        }
    }

    suspend fun setReduceTransparency(value: Boolean) { dataStore.edit { it[ChengmingAccessibilityKeys.ReduceTransparency] = value } }
    suspend fun setIncreaseContrast(value: Boolean) { dataStore.edit { it[ChengmingAccessibilityKeys.IncreaseContrast] = value } }
    suspend fun setReduceMotion(value: Boolean) { dataStore.edit { it[ChengmingAccessibilityKeys.ReduceMotion] = value } }
}
