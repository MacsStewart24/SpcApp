package com.example.smartpowerconnector_room.internet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface AwsUistate{
    data class Success(val device: Flow<List<Device>>): AwsUistate
    object Error: AwsUistate
    object Loading : AwsUistate
}

class AWSViewmodel(private val deviceRepository: DeviceRepository): ViewModel(){
    var awsUistate: AwsUistate by mutableStateOf(AwsUistate.Loading)
        private set

    init{
        getAllDevices()
    }

    fun getAllDevices(){
        viewModelScope.launch {
            awsUistate = AwsUistate.Loading
            awsUistate = try {
                AwsUistate.Success(deviceRepository.getAllDevicesStream())
            }catch ( e: IOException){
                AwsUistate.Error
            }catch (e: HttpException){
                AwsUistate.Error
            }
        }
    }
}