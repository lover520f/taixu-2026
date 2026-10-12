package top.wkbin.taixu.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import top.wkbin.taixu.ui.theme.LocalLiquidGlassSurfaceBackdrop
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import top.wkbin.taixu.ui.theme.ThemeStyle
import top.wkbin.taixu.ui.theme.LocalChengmingAccessibility
import io.github.alexzhirkevich.cupertino.CupertinoSurface

/** Content is the default. Glass is reserved for explicitly floating controls. */
enum class RuntimeSurfaceMaterial { Content, Glass }

/** Shared container: Material card in Xuantong, stable Cupertino content in Chengming. */
@Composable
fun RuntimeCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    borderColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    exportedBackdrop: LayerBackdrop? = null,
    material: RuntimeSurfaceMaterial = RuntimeSurfaceMaterial.Content,
    content: @Composable ColumnScope.() -> Unit,
) {
    val chengming = LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS
    val backdrop = LocalLiquidGlassSurfaceBackdrop.current
    val fill = containerColor.takeOrElse { MaterialTheme.colorScheme.surfaceContainerLow }
    val border = borderColor.takeIf { it.alpha > 0f }?.let { BorderStroke(1.dp, it) }
        ?: if (chengming && LocalChengmingAccessibility.current.increaseContrast) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    if (chengming && material == RuntimeSurfaceMaterial.Glass && backdrop != null &&
        LocalChengmingAccessibility.current.glassEnabled) {
        val interactionSource = remember { MutableInteractionSource() }
        val glassModifier = modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(LiquidGlassLevel.Regular.blurRadius.toPx(), edgeTreatment = TileMode.Mirror)
                    lens(
                        LiquidGlassLevel.Regular.lensMin.toPx(),
                        LiquidGlassLevel.Regular.lensMax.toPx(),
                        depthEffect = LiquidGlassLevel.Regular.depthEffect,
                    )
                },
                highlight = { Highlight.Default.copy(alpha = LiquidGlassLevel.Regular.highlightAlpha) },
                shadow = { Shadow(radius = LiquidGlassLevel.Regular.shadowRadius, alpha = LiquidGlassLevel.Regular.shadowAlpha) },
                innerShadow = { InnerShadow(radius = LiquidGlassLevel.Regular.innerShadowRadius, alpha = LiquidGlassLevel.Regular.innerShadowAlpha) },
                exportedBackdrop = exportedBackdrop,
                onDrawSurface = { drawRoundRect(fill.copy(alpha = 0.72f)) },
            )
            .clip(shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .then(
                if (onClick == null) Modifier else Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ),
            )
        Column(glassModifier.padding(contentPadding), content = content)
        return
    }

    // Composite caller-supplied tint over a stable surface instead of exposing the wallpaper.
    val color = if (chengming) fill.compositeOver(MaterialTheme.colorScheme.surface) else fill
    val surfaceModifier = if (exportedBackdrop == null) modifier else modifier.layerBackdrop(exportedBackdrop)
    if (chengming) {
        if (onClick == null) {
            CupertinoSurface(
                modifier = surfaceModifier.then(if (border != null) Modifier.border(border, shape) else Modifier),
                shape = shape, color = color, contentColor = MaterialTheme.colorScheme.onSurface,
            ) { CupertinoContent { Column(Modifier.padding(contentPadding), content = content) } }
        } else {
            CupertinoSurface(
                onClick = onClick, modifier = surfaceModifier, shape = shape, color = color,
                contentColor = MaterialTheme.colorScheme.onSurface, border = border,
            ) { CupertinoContent { Column(Modifier.padding(contentPadding), content = content) } }
        }
    } else {
        val colors = CardDefaults.cardColors(containerColor = color)
        if (onClick == null) {
            Card(modifier = surfaceModifier, shape = shape, colors = colors, border = border) {
                Column(Modifier.padding(contentPadding), content = content)
            }
        } else {
            Card(onClick = onClick, modifier = surfaceModifier, shape = shape, colors = colors, border = border) {
                Column(Modifier.padding(contentPadding), content = content)
            }
        }
    }
}
