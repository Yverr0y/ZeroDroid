package com.abhishek.zerodroid.features.usb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.zerodroid.core.hardware.HardwareChecker
import com.abhishek.zerodroid.features.usb.domain.UsbDeviceInfo
import com.abhishek.zerodroid.features.usb.domain.UsbDeviceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UsbUiState(
    val devices: List<UsbDeviceInfo> = emptyList(),
    val selectedDevice: UsbDeviceInfo? = null
)

@HiltViewModel
class UsbViewModel @Inject constructor(
    private val usbDeviceManager: UsbDeviceManager,
    hardwareChecker: HardwareChecker
) : ViewModel() {

    val hasUsbHost: Boolean = hardwareChecker.hasUsbHost()

    private val _state = MutableStateFlow(UsbUiState())
    val state: StateFlow<UsbUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            usbDeviceManager.observeDevices().collect { devices ->
                _state.value = _state.value.copy(devices = devices)
            }
        }
    }

    fun selectDevice(device: UsbDeviceInfo?) {
        _state.value = _state.value.copy(selectedDevice = device)
    }
}
