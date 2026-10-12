package top.wkbin.taixu.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.cupertino.CupertinoHorizontalDivider

/** A content list with a shared surface and inset, one-physical-pixel separators. */
@Composable
fun RuntimeGroupedList(
    itemCount: Int,
    modifier: Modifier = Modifier,
    dividerInset: Dp = 16.dp,
    itemContent: @Composable ColumnScope.(Int) -> Unit,
) {
    if (itemCount <= 0) return
    val dividerThickness = (1f / LocalDensity.current.density).dp
    RuntimeCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        repeat(itemCount) { index ->
            itemContent(index)
            if (index < itemCount - 1) {
                CupertinoHorizontalDivider(
                    modifier = Modifier.padding(start = dividerInset),
                    thickness = dividerThickness,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
    }
}
