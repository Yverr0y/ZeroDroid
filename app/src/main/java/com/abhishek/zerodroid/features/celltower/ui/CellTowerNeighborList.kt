package com.abhishek.zerodroid.features.celltower.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.abhishek.zerodroid.core.ui.TerminalCard
import com.abhishek.zerodroid.features.celltower.domain.CellTowerInfo
import com.abhishek.zerodroid.ui.theme.TextDim

// Neighbor cells carry little per-cell info (no full identity, usually) so a full TerminalCard per
// row is mostly repeated chrome — one card with compact, signal-sorted rows scans far faster,
// and a 2G neighbor's red badge is now the kind of thing that visually jumps out of the list.
@Composable
fun CellTowerNeighborList(
    neighbors: List<CellTowerInfo>,
    modifier: Modifier = Modifier
) {
    val sorted = remember(neighbors) { neighbors.sortedByDescending { it.rssi } }

    TerminalCard(modifier = modifier) {
        Text(
            text = "> Neighbor Cells (${sorted.size})",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        sorted.forEachIndexed { index, cell ->
            CellTowerNeighborRow(cell = cell, rank = index + 1)
            if (index != sorted.lastIndex) {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = TextDim.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun CellTowerNeighborRow(cell: CellTowerInfo, rank: Int) {
    val style = cell.type.generationStyle()
    val signalColor = signalColorFor(cell.signalPercent)
    val bgColor = MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$rank",
            style = MaterialTheme.typography.labelSmall,
            color = TextDim,
            modifier = Modifier.width(28.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = style.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = style.color
                )
                cell.pci?.let {
                    Text(
                        text = "  PCI:$it",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextDim
                    )
                }
                cell.arfcn?.let {
                    Text(
                        text = "  ARFCN:$it",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextDim
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                drawRoundRect(color = bgColor, cornerRadius = CornerRadius(2f), size = size)
                drawRoundRect(
                    color = signalColor,
                    cornerRadius = CornerRadius(2f),
                    size = Size(size.width * cell.signalPercent / 100f, size.height)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${cell.rssi} dBm",
            style = MaterialTheme.typography.bodyMedium,
            color = signalColor
        )
    }
}
