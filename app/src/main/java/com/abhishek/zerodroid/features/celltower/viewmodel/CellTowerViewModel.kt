package com.abhishek.zerodroid.features.celltower.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.zerodroid.features.celltower.domain.CellTowerAnalyzer
import com.abhishek.zerodroid.features.celltower.domain.CellTowerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CellTowerViewModel @Inject constructor(
    private val analyzer: CellTowerAnalyzer
) : ViewModel() {

    private val _state = MutableStateFlow(CellTowerState())
    val state: StateFlow<CellTowerState> = _state.asStateFlow()

    private var monitorJob: Job? = null
    private val signalHistory = mutableListOf<Int>()

    fun startMonitoring() {
        if (monitorJob?.isActive == true) return
        monitorJob = viewModelScope.launch {
            analyzer.monitor()
                .catch { e -> _state.value = _state.value.copy(error = e.message, isMonitoring = false) }
                .collect { state ->
                    state.currentCell?.let { cell ->
                        signalHistory.add(cell.rssi)
                        if (signalHistory.size > 60) signalHistory.removeAt(0)
                    }
                    _state.value = state.copy(signalHistory = signalHistory.toList())
                }
        }
    }

    fun stopMonitoring() {
        monitorJob?.cancel()
        monitorJob = null
        _state.value = _state.value.copy(isMonitoring = false)
    }

    override fun onCleared() {
        super.onCleared()
        stopMonitoring()
    }
}
