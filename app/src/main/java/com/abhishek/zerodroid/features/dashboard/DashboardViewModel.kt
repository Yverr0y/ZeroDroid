package com.abhishek.zerodroid.features.dashboard

import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.ViewModel
import com.abhishek.zerodroid.core.di.DashboardPrefs
import com.abhishek.zerodroid.core.hardware.HardwareChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class DeviceInfo(
    val model: String = "${Build.MANUFACTURER.uppercase()} ${Build.MODEL}",
    val androidVersion: String = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
    val device: String = Build.DEVICE,
    val board: String = Build.BOARD
)

data class HardwareItem(
    val name: String,
    val isAvailable: Boolean
)

data class LastUsedFeature(
    val route: String,
    val title: String
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val hardwareChecker: HardwareChecker,
    @DashboardPrefs private val prefs: SharedPreferences
) : ViewModel() {

    val deviceInfo = DeviceInfo()

    private val _hardwareItems = MutableStateFlow<List<HardwareItem>>(emptyList())
    val hardwareItems: StateFlow<List<HardwareItem>> = _hardwareItems.asStateFlow()

    private val _lastUsedFeature = MutableStateFlow<LastUsedFeature?>(null)
    val lastUsedFeature: StateFlow<LastUsedFeature?> = _lastUsedFeature.asStateFlow()

    init {
        _hardwareItems.value = listOf(
            HardwareItem("WiFi", hardwareChecker.hasWifi()),
            HardwareItem("Bluetooth", hardwareChecker.hasBluetooth()),
            HardwareItem("BLE", hardwareChecker.hasBluetoothLe()),
            HardwareItem("NFC", hardwareChecker.hasNfc()),
            HardwareItem("IR", hardwareChecker.hasIr()),
            HardwareItem("Camera", hardwareChecker.hasCamera()),
            HardwareItem("GPS", hardwareChecker.hasGps()),
            HardwareItem("USB Host", hardwareChecker.hasUsbHost()),
            HardwareItem("UWB", hardwareChecker.hasUwb()),
            HardwareItem("Wi-Fi Aware", hardwareChecker.hasWifiAware()),
            HardwareItem("Wi-Fi Direct", hardwareChecker.hasWifiDirect()),
            HardwareItem("Telephony", hardwareChecker.hasTelephony()),
            HardwareItem("Gyroscope", hardwareChecker.hasGyroscope()),
            HardwareItem("Barometer", hardwareChecker.hasBarometer())
        )

        val route = prefs.getString(KEY_LAST_ROUTE, null)
        val title = prefs.getString(KEY_LAST_TITLE, null)
        if (route != null && title != null) {
            _lastUsedFeature.value = LastUsedFeature(route, title)
        }
    }

    fun saveLastUsed(route: String, title: String) {
        prefs.edit()
            .putString(KEY_LAST_ROUTE, route)
            .putString(KEY_LAST_TITLE, title)
            .apply()
        _lastUsedFeature.value = LastUsedFeature(route, title)
    }

    companion object {
        private const val KEY_LAST_ROUTE = "last_used_route"
        private const val KEY_LAST_TITLE = "last_used_title"
    }
}
