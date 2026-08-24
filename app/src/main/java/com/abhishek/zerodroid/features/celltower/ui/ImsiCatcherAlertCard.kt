package com.abhishek.zerodroid.features.celltower.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhishek.zerodroid.core.ui.TerminalCard
import com.abhishek.zerodroid.features.celltower.domain.AlertSeverity
import com.abhishek.zerodroid.features.celltower.domain.ImsiCatcherAlert
import com.abhishek.zerodroid.ui.theme.SeverityHigh
import com.abhishek.zerodroid.ui.theme.SeverityLow
import com.abhishek.zerodroid.ui.theme.SeverityMedium

@Composable
fun ImsiCatcherAlertCard(
    alert: ImsiCatcherAlert,
    modifier: Modifier = Modifier
) {
    val severityColor = when (alert.severity) {
        AlertSeverity.HIGH -> SeverityHigh
        AlertSeverity.MEDIUM -> SeverityMedium
        AlertSeverity.LOW -> SeverityLow
    }

    TerminalCard(
        modifier = modifier,
        borderColor = severityColor,
        glowColor = severityColor,
        animated = alert.severity == AlertSeverity.HIGH
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "[${alert.severity}]",
                style = MaterialTheme.typography.labelSmall,
                color = severityColor
            )
            Text(
                text = " ${alert.type.name}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = alert.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
