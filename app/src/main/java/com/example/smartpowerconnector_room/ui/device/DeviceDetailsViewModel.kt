package com.example.smartpowerconnector_room.ui.device

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.internet.idata.*
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.*
import java.io.IOException

class DeviceDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository,
    private val awsRepository: AwsRepository
): ViewModel() {

    private val deviceID: Int = checkNotNull(savedStateHandle[DeviceDetailsDestination.deviceIDArg])
    val uiState: StateFlow<DeviceDetailsUiState> =
        deviceRepository.getDeviceStream(deviceID)
            .filterNotNull()
            .map{
               DeviceDetailsUiState(statusChange = it.deviceStatus == "Off", deviceDetails = it.toDeviceDetails() )// Reverted change noKnownDevices= it.deviceName == null,

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DeviceDetailsUiState()
            )

    suspend fun changeStatus() {
        val currentDevice = uiState.value.deviceDetails.toDevice()
        val updatedDevice = currentDevice.copy(
            deviceStatus = if (currentDevice.deviceStatus == "Off") "On" else "Off"
        )
        deviceRepository.updateDevice(updatedDevice)
        val updatedNetworkDevice = uiState.value.deviceDetails.toDeviceData()
        awsRepository.onOffSwitch(updatedNetworkDevice)
    }
        suspend fun deleteDevice(){
            deviceRepository.deleteDevice(uiState.value.deviceDetails.toDevice())
        }

        companion object{
            private const val TIMEOUT_MILLIS = 5_000L
        }
    }

data class DeviceDetailsUiState(
    //val noKnownDevices: Boolean = true,
    val statusChange: Boolean = true,
    val deviceDetails: DeviceDetails = DeviceDetails()
)

fun DeviceDetails.toDeviceData(): DeviceData {
    return DeviceData(
        deviceName = this.deviceName,
        deviceId= this.deviceId,
        deviceDescription =  this.deviceDescription,
        deviceStatus = this.deviceStatus
    )
}
//This code updates the server but crashes
/* suspend fun changeStatus() {
     val currentDevice = uiState.value.deviceDetails.toDevice()
     val updatedDevice = currentDevice.copy(
         deviceStatus = if (currentDevice.deviceStatus == "Off") "On" else "Off"
     )
     deviceRepository.updateDevice(updatedDevice)
     val updatedNetworkDevice = uiState.value.deviceDetails.toDeviceData()
     onOffSwitch(updatedNetworkDevice)
 }*/
//This code chagnes the device status locally and doesn't crash. But doesn't update the server,
/*suspend fun changeStatus() {
        val currentDevice = uiState.value.deviceDetails.toDevice()
        val updatedDevice = currentDevice.copy(
            deviceStatus = if (currentDevice.deviceStatus == "Off") "On" else "Off"
        )
        deviceRepository.updateDevice(updatedDevice)
        val updatedNetworkDevice = uiState.value.deviceDetails.toDeviceData()
        onOffSwitch(updatedNetworkDevice)

        // Create a new DeviceDetailsUiState with the updated device status
        val updatedUiState = uiState.value.copy(
            statusChange = updatedDevice.deviceStatus == "Off",
            deviceDetails = updatedDevice.toDeviceDetails()
        )

        // Pass the updated UiState to stateIn
        uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = updatedUiState
        )
    }


    fun onOffSwitch(deviceData: DeviceData) {
        viewModelScope.launch {
        awsUiState = AwsUiState.Loading
        awsUiState = try {
            awsRepository.onOffSwitch(deviceData)
            AwsUiState.Success(awsRepository.getAllDevices()) // refresh the device list after updating
        } catch (e: IOException) {
            AwsUiState.Error
        } catch (e: HttpException) {
            AwsUiState.Error
        }
    }
}*/