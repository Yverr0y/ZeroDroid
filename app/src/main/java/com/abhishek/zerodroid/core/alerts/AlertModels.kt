package com.abhishek.zerodroid.core.alerts

enum class AlertSource(val label: String) {
    ROGUE_AP("Rogue AP"),
    DEAUTH("Deauth Detector"),
    GPS_SPOOF("GPS Spoof"),
    HIDDEN_CAMERA("Hidden Camera"),
    BLUETOOTH_TRACKER("Tracker Scanner")
}

enum class AlertSeverity { CRITICAL, HIGH, MEDIUM, LOW }

data class UnifiedAlert(
    val id: String,
    val source: AlertSource,
    val severity: AlertSeverity,
    val title: String,
    val detail: String,
    val timestamp: Long
)
