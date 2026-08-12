package com.abhishek.zerodroid.features.usbcamera.viewmodel

import androidx.lifecycle.ViewModel
import com.abhishek.zerodroid.core.hardware.HardwareChecker
import com.abhishek.zerodroid.features.usbcamera.domain.UsbCameraDetector
import com.abhishek.zerodroid.features.usbcamera.domain.UsbCameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UsbCameraViewModel @Inject constructor(
    private val detector: UsbCameraDetector,
    hardwareChecker: HardwareChecker
) : ViewModel() {

    private val _state = MutableStateFlow(UsbCameraState(hasUsbHost = hardwareChecker.hasUsbHost()))
    val state: StateFlow<UsbCameraState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.value = _state.value.copy(
            usbVideoDevices = detector.detectUsbVideoDevices(),
            camera2ExternalCameras = detector.detectCamera2External()
        )
    }

}
