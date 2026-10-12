package top.wkbin.taixu.ui.theme

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/** Adapt the library's legacy indication to modern Compose nodes without a Material ripple. */
internal class ChengmingIndication(private val color: Color) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): Modifier.Node = object : Modifier.Node(), DrawModifierNode {
        private val presses = mutableSetOf<PressInteraction.Press>()
        private var focused = false
        override fun onAttach() {
            coroutineScope.launch {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> presses.add(interaction)
                        is PressInteraction.Release -> presses.remove(interaction.press)
                        is PressInteraction.Cancel -> presses.remove(interaction.press)
                        is FocusInteraction.Focus -> focused = true
                        is FocusInteraction.Unfocus -> focused = false
                    }
                    invalidateDraw()
                }
            }
        }
        override fun ContentDrawScope.draw() {
            drawContent()
            if (presses.isNotEmpty()) drawRect(color.copy(alpha = 0.10f))
            if (focused) drawRect(color, style = Stroke(2.dp.toPx()))
        }
        override fun onDetach() { presses.clear(); focused = false }
    }
    override fun equals(other: Any?) = other is ChengmingIndication && other.color == color
    override fun hashCode() = color.hashCode()
}
