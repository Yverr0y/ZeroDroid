package com.abhishek.zerodroid.features.sdr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhishek.zerodroid.core.ui.TerminalCard
import com.abhishek.zerodroid.features.sdr.domain.SdrDeviceInfo
import com.abhishek.zerodroid.ui.theme.TerminalGreen

@Composable
fun SdrDeviceCard(
    device: SdrDeviceInfo,
    isConnecting: Boolean,
    isConnected: Boolean,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    TerminalCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.chipset,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "VID:PID ${device.vidPid}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = device.deviceName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (device.isRtlSdr) {
                Text(
                    text = "RTL-SDR",
                    style = MaterialTheme.typography.labelSmall,
                    color = TerminalGreen
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        when {
            isConnecting -> CircularProgressIndicator(modifier = Modifier.height(24.dp))
            isConnected -> Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Connected — interface claimed",
                    style = MaterialTheme.typography.labelSmall,
                    color = TerminalGreen,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = onDisconnect) { Text("Disconnect") }
            }
            else -> Button(
                onClick = onConnect,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Connect") }
        }
    }
}
