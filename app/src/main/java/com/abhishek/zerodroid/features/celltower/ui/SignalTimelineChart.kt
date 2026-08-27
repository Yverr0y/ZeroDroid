package com.abhishek.zerodroid.features.celltower.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.abhishek.zerodroid.core.ui.TerminalCard
import com.abhishek.zerodroid.ui.theme.TextDim
import kotlin.math.roundToInt

private const val MIN_SIGNAL = -120f
private const val MAX_SIGNAL = -30f

@Composable
fun SignalTimelineChart(
    signalHistory: List<Int>,
    modifier: Modifier = Modifier
) {
    if (signalHistory.isEmpty()) return

    val currentPercent = when {
        signalHistory.last() >= -70 -> 100
        signalHistory.last() <= -120 -> 0
        else -> ((signalHistory.last() + 120) * 100) / 50
    }
    val lineColor = signalColorFor(currentPercent)
    val gridColor = TextDim.copy(alpha = 0.25f)
    val range = MAX_SIGNAL - MIN_SIGNAL

    TerminalCard(modifier = modifier) {
        Text(text = "> Signal Timeline", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End
            ) {
                for (i in 0..4) {
                    val dbm = (MAX_SIGNAL - range * i / 4).roundToInt()
                    Text(text = "$dbm", style = MaterialTheme.typography.labelSmall, color = TextDim)
                    if (i < 4) Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.width(6.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Grid lines
                    for (i in 0..4) {
                        val y = size.height * i / 4
                        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
                    }

                    if (signalHistory.size < 2) return@Canvas

                    val stepX = size.width / (signalHistory.size - 1).coerceAtLeast(1)

                    fun yFor(signal: Int): Float {
                        val normalized = 1f - ((signal - MIN_SIGNAL) / range).coerceIn(0f, 1f)
                        return normalized * size.height
                    }

                    val linePath = Path()
                    signalHistory.forEachIndexed { index, signal ->
                        val x = index * stepX
                        val y = yFor(signal)
                        if (index == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
                    }

                    // Filled area under the line
                    val fillPath = Path().apply {
                        addPath(linePath)
                        lineTo((signalHistory.size - 1) * stepX, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(
                        fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0f))
                        )
                    )

                    // Glow + line
                    drawPath(linePath, lineColor.copy(alpha = 0.15f), style = Stroke(width = 6f))
                    drawPath(linePath, lineColor, style = Stroke(width = 2f))

                    // Current value dot
                    val lastX = (signalHistory.size - 1) * stepX
                    val lastY = yFor(signalHistory.last())
                    drawCircle(lineColor, radius = 4f, center = Offset(lastX, lastY))
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Current: ${signalHistory.lastOrNull() ?: 0} dBm | Min: ${signalHistory.min()} | Max: ${signalHistory.max()} | Samples: ${signalHistory.size}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
