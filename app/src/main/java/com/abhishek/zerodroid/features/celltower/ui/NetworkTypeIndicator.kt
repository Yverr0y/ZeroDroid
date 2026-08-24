package com.abhishek.zerodroid.features.celltower.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhishek.zerodroid.core.ui.TerminalCard
import com.abhishek.zerodroid.features.celltower.domain.CellType

@Composable
fun NetworkTypeIndicator(
    cellType: CellType,
    signalStrength: Int,
    modifier: Modifier = Modifier
) {
    val style = cellType.generationStyle()

    TerminalCard(modifier = modifier, glowColor = style.color, animated = true) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "> Network Type", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = style.label, style = MaterialTheme.typography.displaySmall, color = style.color)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Signal", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "$signalStrength dBm", style = MaterialTheme.typography.headlineSmall, color = style.color)
            }
        }
    }
}
