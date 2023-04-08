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
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    var deviceUiState by mutableStateOf( DeviceUiState() )
        private set

    private val deviceID: Int = checkNotNull(savedStateHandle[DeviceEditDestination.deviceIDArg])

    init{
        viewModelScope.launch{
            deviceUiState = deviceRepository.getDeviceStream(deviceID)
                .filterNotNull()
                .first()
                .toDeviceUiState(true)
        }
    }
    suspend fun updateDevice() {
        if (validateInput(deviceUiState.DeviceDetails)) {
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

//might need to add function Reduce by one
/*suspend fun changeStatus(){
    viewModelScope.launch {
        val currentDevice = uiState.value.deviceDetails.toDevice()
        if(currentDevice.deviceStatus == "Off"){
            deviceRepository.updateDevice(currentDevice.copy(deviceStatus = "On"))
        }
        else{
            deviceRepository.updateDevice(currentDevice.copy(deviceStatus = "Off"))
        }
        //POST will be implemented Here
    }
}
*/
