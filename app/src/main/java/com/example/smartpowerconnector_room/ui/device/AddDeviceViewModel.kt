package com.example.smartpowerconnector_room.ui.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository

class AddDeviceViewModel(private val deviceRepository: DeviceRepository) : ViewModel(){
        var deviceUiState by mutableStateOf( DeviceUiState() )
            private set

        fun updateUiState(deviceDetails: DeviceDetails){
            deviceUiState = DeviceUiState(DeviceDetails = deviceDetails, validEntry = validateInput(deviceDetails))
        }

        suspend fun saveDevice(){
            if(validateInput()){
                deviceRepository.insertDevice(deviceUiState.DeviceDetails.toDevice())
            }
        }
        private fun validateInput(uiState: DeviceDetails = deviceUiState.DeviceDetails): Boolean{
            return with(uiState){
                deviceName.isNotBlank() && deviceId.isNotBlank()
            }
        }
    }

data class DeviceDetails(
    val id: Int = 0,
    val deviceName: String = " ",
    val deviceId: String= " ",
    val deviceStatus: String = "Off",
    val deviceDescription: String= " ",
    val deviceTime: Int = 0
)

data class DeviceUiState(
    val DeviceDetails: DeviceDetails = DeviceDetails(),
    val validEntry: Boolean = false
)

fun DeviceDetails.toDevice(): Device = Device(
    id = id,
    deviceName= deviceName,
    deviceId = deviceId,
    deviceStatus= deviceStatus,
    deviceDescription = deviceDescription,
)

fun Device.toDeviceUiState(validEntry: Boolean= false): DeviceUiState = DeviceUiState(
    DeviceDetails = this.toDeviceDetails(),
    validEntry = validEntry
)

fun Device.toDeviceDetails(): DeviceDetails = DeviceDetails(
    id = id,
    deviceName= deviceName,
    deviceId = deviceId,
    deviceStatus= deviceStatus,
    deviceDescription = deviceDescription,
)