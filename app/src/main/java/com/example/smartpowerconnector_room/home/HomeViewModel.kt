package com.example.smartpowerconnector_room.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository
import kotlinx.coroutines.flow.*


class HomeViewModel(deviceRepository: DeviceRepository): ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        deviceRepository.getAllDevicesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val deviceList: List<Device> = listOf())





