package com.abhishek.zerodroid.features.wardriving.ui

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
import com.abhishek.zerodroid.core.util.FrequencyUtils
import com.abhishek.zerodroid.core.util.SecurityType
import com.abhishek.zerodroid.features.wardriving.domain.WardrivingRecord
import com.abhishek.zerodroid.ui.theme.TerminalGreen
import com.abhishek.zerodroid.ui.theme.TextDim
import java.text.SimpleDateFormat
import java.util.Locale

// This card leads with what makes a wardriving record distinct from a plain WiFi Analyzer
// row -- where and when the AP was seen -- rather than repeating SSID/BSSID/RSSI as the
// primary content, since that data is already fully visible in WiFi Analyzer.
private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

@Composable
fun WardrivingRecordItem(
    record: WardrivingRecord,
    modifier: Modifier = Modifier
) {
    val security = SecurityType.fromCapabilities(record.capabilities ?: "")
    val band = if (record.frequency > 0) FrequencyUtils.frequencyToBand(record.frequency) else null

    TerminalCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "%.5f, %.5f".format(record.lat, record.lng),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TerminalGreen
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${record.ssid ?: "<hidden>"} · ${record.bssid}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDim
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timeFormat.format(record.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "${record.rssi}dBm",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextDim
                    )
                    band?.let {
                        Text(
                            text = it.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextDim
                        )
                    }
                    Text(
                        text = security.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextDim
                    )
                }
            }
        }
    }
}
