package top.wkbin.taixu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import top.wkbin.taixu.ui.theme.ThemeStyle

/** Decorative content icon; glass belongs to the surrounding navigation/action control. */
@Composable
fun IconTile(
    icon: RuntimeIconName,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 42.dp,
) {
    val chengming = LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS
    // 澄明模式下：将默认或大磁贴收敛为 iOS 原生标准的 30dp；保留显式声明的小尺寸
    val effectiveSize = if (chengming && size >= 34.dp) 30.dp else size
    val cornerRadius = if (chengming) (if (effectiveSize <= 32.dp) 7.dp else 10.dp) else (if (effectiveSize <= 32.dp) 8.dp else 12.dp)
    val fill = if (chengming) color else color.copy(alpha = 0.12f)
    val foreground = if (chengming) Color.White else color
    val iconSize = if (chengming && effectiveSize <= 32.dp) 16.dp else (effectiveSize * 0.48f)
    Box(
        modifier = modifier.size(effectiveSize)
            .clip(RoundedCornerShape(cornerRadius))
            .background(fill),
        contentAlignment = Alignment.Center,
    ) {
        RuntimeIcon(icon, Modifier.size(iconSize), tint = foreground)
    }
}
