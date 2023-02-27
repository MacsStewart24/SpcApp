package com.example.smartpowerconnector_room.ui.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpowerconnector_room.data.DeviceRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditDeviceViewModel(
    savedStatehandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    var deviceUiState by mutableStateOf( DeviceUiState() )
        private set

    private val deviceId: Int = checkNotNull(savedStatehandle[DeviceEditDestination.deviceIDArg])

    init{
        viewModelScope.launch{
            deviceUiState = deviceRepository.getDeviceStream(deviceId)
                .filterNotNull()
                .first()
                .toDeviceUiState(true)
        }
    }
    suspend fun updateDevice(){
        if(validateInput(deviceUiState.DeviceDetails)){
            deviceRepository.updateDevice(deviceUiState.DeviceDetails.toDevice())
        }
    }

    fun updateUiState(deviceDetails: DeviceDetails){
        deviceUiState = DeviceUiState(DeviceDetails = deviceDetails, validEntry = validateInput(deviceDetails))
    }

    private fun validateInput(uiState: DeviceDetails = deviceUiState.DeviceDetails): Boolean{
        return with (uiState){
            deviceName.isNotBlank() && deviceId.isNotBlank()
        }
    }
}