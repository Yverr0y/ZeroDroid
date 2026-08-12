package com.abhishek.zerodroid.features.uwb.viewmodel

import androidx.lifecycle.ViewModel
import com.abhishek.zerodroid.features.uwb.domain.UwbService
import com.abhishek.zerodroid.features.uwb.domain.UwbState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UwbViewModel @Inject constructor(
    uwbService: UwbService
) : ViewModel() {

    private val _state = MutableStateFlow(
        UwbState(
            isHardwareAvailable = uwbService.isAvailable,
            deviceInfo = uwbService.getDeviceInfo()
        )
    )
    val state: StateFlow<UwbState> = _state.asStateFlow()
}
