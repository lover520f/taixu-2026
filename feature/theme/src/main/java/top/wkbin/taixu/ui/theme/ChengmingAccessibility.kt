package top.wkbin.taixu.ui.theme

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import top.wkbin.taixu.core.model.ChengmingAccessibilityOptions

@Immutable
data class ChengmingAccessibilityPolicy(
    val reduceTransparency: Boolean = false,
    val increaseContrast: Boolean = false,
    val reduceMotion: Boolean = false,
) {
    val glassEnabled get() = !reduceTransparency && !increaseContrast
}

val LocalChengmingAccessibility = staticCompositionLocalOf { ChengmingAccessibilityPolicy() }

/** Observe system accessibility/animation settings as well as the persisted app choices. */
@Composable
internal fun rememberChengmingAccessibility(options: ChengmingAccessibilityOptions): ChengmingAccessibilityPolicy {
    val resolver = LocalContext.current.contentResolver
    fun readSystem() = Pair(
        Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f,
        Settings.Secure.getInt(resolver, "high_text_contrast_enabled", 0) == 1,
    )
    var system by remember(resolver) { mutableStateOf(readSystem()) }
    DisposableEffect(resolver) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) { system = readSystem() }
        }
        resolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE), false, observer)
        resolver.registerContentObserver(Settings.Secure.getUriFor("high_text_contrast_enabled"), false, observer)
        onDispose { resolver.unregisterContentObserver(observer) }
    }
    return ChengmingAccessibilityPolicy(
        reduceTransparency = options.reduceTransparency,
        increaseContrast = options.increaseContrast || system.second,
        reduceMotion = options.reduceMotion || system.first,
    )
}

internal fun ColorScheme.withChengmingContrast(): ColorScheme {
    val dark = onSurface.luminance() > 0.5f
    return copy(
        onSurface = if (dark) Color.White else Color.Black,
        onBackground = if (dark) Color.White else Color.Black,
        onSurfaceVariant = if (dark) Color.White else Color.Black,
        outline = if (dark) Color.White else Color.Black,
        outlineVariant = if (dark) Color(0xFF8E8E93) else Color(0xFF636366),
    )
}
