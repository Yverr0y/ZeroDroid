package com.abhishek.zerodroid.features.sdr.viewmodel

import androidx.lifecycle.ViewModel
import com.abhishek.zerodroid.core.hardware.HardwareChecker
import com.abhishek.zerodroid.features.sdr.domain.SdrDetector
import com.abhishek.zerodroid.features.sdr.domain.SdrState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SdrViewModel @Inject constructor(
    private val sdrDetector: SdrDetector,
    hardwareChecker: HardwareChecker
) : ViewModel() {

    private val _state = MutableStateFlow(SdrState(hasUsbHost = hardwareChecker.hasUsbHost()))
    val state: StateFlow<SdrState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.value = _state.value.copy(devices = sdrDetector.detect())
    }

}
