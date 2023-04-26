package com.example.smartpowerconnector_room.usage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import com.example.smartpowerconnector_room.internet.idata.DeviceData
import com.example.smartpowerconnector_room.internet.idata.Usage
import com.example.smartpowerconnector_room.internet.idata.UseRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface UseUiState{
   data class Success(val iDeviceList: List<Usage>): UseUiState
//   data class Success(val iDeviceList: List<DeviceData>): UseUiState
    object Error: UseUiState
    object Loading: UseUiState
}

class UsageViewModel(private val useRepository: UseRepository) :ViewModel() {
    var useUiState: UseUiState by mutableStateOf(UseUiState.Loading)
        private set


    init{
        getUsages()
    }

    fun getUsages(){
        viewModelScope.launch {
            useUiState = UseUiState.Loading
            useUiState = try{
                UseUiState.Success(useRepository.getUsages())
            }catch (e: IOException){
                UseUiState.Error
            }catch(e: HttpException) {
                UseUiState.Error
            }
        }
    }
}



