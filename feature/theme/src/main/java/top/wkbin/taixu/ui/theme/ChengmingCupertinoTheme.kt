package top.wkbin.taixu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.foundation.LocalIndication
import androidx.compose.ui.graphics.luminance
import io.github.alexzhirkevich.cupertino.theme.CupertinoTheme
import io.github.alexzhirkevich.cupertino.theme.Typography
import io.github.alexzhirkevich.cupertino.theme.darkColorScheme
import io.github.alexzhirkevich.cupertino.theme.lightColorScheme

/** The library and legacy Material slots share the same semantic palette and Android typeface. */
@Composable
internal fun ChengmingCupertinoTheme(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val base = if (colors.onSurface.luminance() > 0.5f) darkColorScheme() else lightColorScheme()
    CupertinoTheme(
        colorScheme = base.copy(
            accent = colors.primary, label = colors.onSurface, secondaryLabel = colors.onSurfaceVariant,
            tertiaryLabel = colors.onSurfaceVariant, quaternaryLabel = colors.onSurfaceVariant,
            systemBackground = colors.surface, secondarySystemBackground = colors.surfaceContainer,
            tertiarySystemBackground = colors.surfaceContainerHigh,
            systemGroupedBackground = colors.background, secondarySystemGroupedBackground = colors.surfaceContainerLow,
            tertiarySystemGroupedBackground = colors.surfaceContainerHigh,
            separator = colors.outlineVariant, opaqueSeparator = colors.outlineVariant, link = colors.primary,
        ),
        typography = Typography(
            largeTitle = AppleTypography.LargeTitle, title1 = AppleTypography.Title1,
            title2 = AppleTypography.Title2, title3 = AppleTypography.Title3,
            headline = AppleTypography.Headline, body = AppleTypography.Body,
            callout = AppleTypography.Callout, subhead = AppleTypography.Subhead,
            footnote = AppleTypography.Footnote, caption1 = AppleTypography.Caption1, caption2 = AppleTypography.Caption2,
        ),
        content = {
            CompositionLocalProvider(LocalIndication provides remember(colors.onSurface) { ChengmingIndication(colors.onSurface) },
                content = content)
        },
    )
}
