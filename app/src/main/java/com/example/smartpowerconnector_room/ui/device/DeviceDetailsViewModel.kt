package com.example.smartpowerconnector_room.ui.device

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpowerconnector_room.data.DeviceRepository
import kotlinx.coroutines.flow.*

class DeviceDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository
        ): ViewModel() {
    private val deviceID: Int = checkNotNull(savedStateHandle[DeviceDetailsDestination.deviceIDArg])

    val uiState: StateFlow<DeviceDetailsUiState> =
        deviceRepository.getDeviceStream(deviceID)
            .filterNotNull()
            .map{
               DeviceDetailsUiState(
                   noKnownDevices= it.deviceName == null, deviceDetails = it.toDeviceDetails()
               )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DeviceDetailsUiState()
            )
        suspend fun deleteDevice(){
            deviceRepository.deleteDevice(uiState.value.deviceDetails.toDevice())
        }

        companion object{
            private const val TIMEOUT_MILLIS = 5_000L
        }
    }

data class DeviceDetailsUiState(
    val noKnownDevices: Boolean = true,
    val deviceDetails: DeviceDetails = DeviceDetails()
)