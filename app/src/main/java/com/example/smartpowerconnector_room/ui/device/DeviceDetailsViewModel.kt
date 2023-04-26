package com.example.smartpowerconnector_room.ui.device


import androidx.lifecycle.*
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutine
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutine
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutine
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.routine.RoutinesRepository
import com.example.smartpowerconnector_room.data.allRoutine.routine.TimerRoutine
import com.example.smartpowerconnector_room.internet.idata.*
import kotlinx.coroutines.flow.*

class DeviceDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository,
    private val awsRepository: AwsRepository,
    private val routinesRepository: RoutinesRepository,
    private val clockRoutineRepository: ClockRoutineRepository,
    private val multiRoutineRepository: MultiRoutineRepository,
    private val mixRoutineRepository: MixRoutineRepository
    ): ViewModel() {

    private val deviceID: Int = checkNotNull(savedStateHandle[DeviceDetailsDestination.deviceIDArg])
    val uiState: StateFlow<DeviceDetailsUiState> =
        deviceRepository.getDeviceStream(deviceID)
            .filterNotNull()
            .map {
                DeviceDetailsUiState(
                    statusChange = it.deviceStatus == "Off",
                    deviceDetails = it.toDeviceDetails()
                )// Reverted change noKnownDevices= it.deviceName == null,

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DeviceDetailsUiState()
            )

    val itemUiState2: StateFlow<ItemUiState2> =
        routinesRepository.getAllTimerRoutinesStream().map { ItemUiState2(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(DeviceDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState2()
            )

    val itemUiState3: StateFlow<ItemUiState3> =
        clockRoutineRepository.getAllClockRoutinesStream().map { ItemUiState3(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(DeviceDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState3()
            )

    val itemUiState4: StateFlow<ItemUiState4> =
        multiRoutineRepository.getAllMultiRoutinesStream().map { ItemUiState4(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(DeviceDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState4()
            )

    val itemUiState5: StateFlow<ItemUiState5> =
        mixRoutineRepository.getAllMixRoutinesStream().map { ItemUiState5(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(DeviceDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState5()
            )

    suspend fun changeStatus() {
        val device = uiState.value.deviceDetails.toDevice()
        val currentDevice = uiState.value.deviceDetails.toDeviceData()
        val updatedDevice = currentDevice.copy(
            deviceStatus = if (currentDevice.deviceStatus == "Off") "On" else "Off"
        )
        awsRepository.onOffSwitch(updatedDevice)
        val repoDevice = device.copy(
            deviceStatus = if (currentDevice.deviceStatus == "Off") "On" else "Off"
        )
        deviceRepository.updateDevice(repoDevice)
    }

        suspend fun deleteDevice(){
            deviceRepository.deleteDevice(uiState.value.deviceDetails.toDevice())
            awsRepository.deleteDevice(uiState.value.deviceDetails.toDeviceData())
        }

        companion object{
            private const val TIMEOUT_MILLIS = 5_000L
        }
    }

data class DeviceDetailsUiState(
    val statusChange: Boolean = true,
    val deviceDetails: DeviceDetails = DeviceDetails()
)

fun DeviceDetails.toDeviceData(): DeviceData {
    return DeviceData(
        deviceName = this.deviceName,
        deviceId= this.deviceId,
        deviceDescription =  this.deviceDescription,
        deviceStatus = this.deviceStatus,
//        deviceTime = this.deviceTime
    )
}

data class ItemUiState2(val timerRoutineList: List<TimerRoutine> = listOf())

data class ItemUiState3(val clockRoutineList: List<ClockRoutine> = listOf())

data class ItemUiState4(val multiRoutineList: List<MultiRoutine> = listOf())

data class ItemUiState5(val mixRoutineList: List<MixRoutine> = listOf())