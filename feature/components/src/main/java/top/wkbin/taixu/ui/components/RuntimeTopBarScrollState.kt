package top.wkbin.taixu.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Stable
class RuntimeTopBarScrollState internal constructor(private val range: Float) {
    var offset by mutableFloatStateOf(0f)
        private set
    val collapsedFraction get() = (offset / range).coerceIn(0f, 1f)
    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            offset = (offset - consumed.y).coerceIn(0f, range)
            return Offset.Zero
        }
    }
}

@Composable
fun rememberRuntimeTopBarScrollState(): RuntimeTopBarScrollState {
    val range = with(LocalDensity.current) { 64.dp.toPx() }
    return remember(range) { RuntimeTopBarScrollState(range) }
}
