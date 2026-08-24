package com.abhishek.zerodroid.features.celltower.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.abhishek.zerodroid.features.celltower.domain.CellType
import com.abhishek.zerodroid.ui.theme.TerminalAmber
import com.abhishek.zerodroid.ui.theme.TerminalCyan
import com.abhishek.zerodroid.ui.theme.TerminalGreen
import com.abhishek.zerodroid.ui.theme.TerminalRed

// A 2G neighbor is itself a security signal (forced-downgrade risk), so generation is color-coded
// the same way everywhere it appears — a stray red badge in the neighbor list should catch the eye.
data class CellGenerationStyle(val label: String, val color: Color)

@Composable
fun CellType.generationStyle(): CellGenerationStyle = when (this) {
    CellType.NR -> CellGenerationStyle("5G", TerminalCyan)
    CellType.LTE -> CellGenerationStyle("LTE", TerminalGreen)
    CellType.WCDMA, CellType.TDSCDMA -> CellGenerationStyle("3G", TerminalAmber)
    CellType.GSM -> CellGenerationStyle("2G", TerminalRed)
    CellType.CDMA -> CellGenerationStyle("CDMA", TerminalAmber)
    CellType.UNKNOWN -> CellGenerationStyle("?", MaterialTheme.colorScheme.onSurfaceVariant)
}

fun signalColorFor(signalPercent: Int): Color = when {
    signalPercent >= 60 -> TerminalGreen
    signalPercent >= 30 -> TerminalAmber
    else -> TerminalRed
}
