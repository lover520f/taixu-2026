package top.wkbin.taixu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import top.wkbin.taixu.ui.theme.ThemeStyle
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import top.wkbin.taixu.ui.theme.LocalLiquidGlassSurfaceBackdrop
import top.wkbin.taixu.ui.components.GlassEffects.liquidGlassSurface

/** Ordinary Chengming content uses an opaque panel; navigation chrome may opt into glass. */
@Composable
fun TaiXuGlassPanel(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    forceDark: Boolean = false,
    omitTopBorder: Boolean = false,
    showTopHighlight: Boolean = true,
    surfaceColor: Color? = null,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val isDark = forceDark || MaterialTheme.colorScheme.onSurface.luminance() > 0.5f
    if (LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS && !LocalCupertinoChrome.current) {
        Box(modifier.clip(shape).background((surfaceColor ?: MaterialTheme.colorScheme.surface)
            .compositeOver(MaterialTheme.colorScheme.surface)), content = content)
        return
    }
    val glassBackdrop = LocalLiquidGlassSurfaceBackdrop.current

    val topTint = if (isDark) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
    } else {
        Color.White.copy(alpha = 0.75f)
    }
    val bottomTint = if (isDark) {
        MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.25f)
    } else {
        Color.White.copy(alpha = 0.50f)
    }

    val borderColor = if (isDark) {
        Color.White.copy(alpha = 0.08f)
    } else {
        Color.White.copy(alpha = 0.85f)
    }

    val panelModifier = if (glassBackdrop != null) {
        val glassSurface = surfaceColor ?: if (isDark) {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.38f)
        } else {
            Color.White.copy(alpha = 0.50f)
        }
        modifier
            .liquidGlassSurface(
                backdrop = glassBackdrop,
                shape = { shape },
                level = LiquidGlassLevel.Thick,
                onDrawSurface = { drawRect(glassSurface) },
            )
            .clip(shape)
            .then(
                if (!omitTopBorder) {
                    Modifier.border(borderWidth, borderColor.copy(alpha = if (isDark) 0.12f else 0.40f), shape)
                } else {
                    Modifier
                }
            )
    } else {
        val backgroundBrush = if (surfaceColor != null) {
            Brush.verticalGradient(listOf(surfaceColor, surfaceColor))
        } else {
            Brush.verticalGradient(listOf(topTint, bottomTint))
        }
        modifier
            .clip(shape)
            .background(backgroundBrush, shape)
            .then(
                if (!omitTopBorder) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else {
                    Modifier
                }
            )
    }

    Box(modifier = panelModifier) {
        content()

        // 顶部 1px 镜面高光反射线
        if (showTopHighlight && !omitTopBorder) {
            val highlightBrush = Brush.horizontalGradient(
                listOf(
                    Color.White.copy(alpha = 0.05f),
                    Color.White.copy(alpha = if (isDark) 0.35f else 0.80f),
                    Color.White.copy(alpha = 0.05f),
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.TopCenter)
                    .background(highlightBrush),
            )
        }
    }
}

