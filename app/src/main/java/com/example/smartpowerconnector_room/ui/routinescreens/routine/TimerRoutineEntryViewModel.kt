/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.smartpowerconnector_room.ui.routinescreens.routine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpowerconnector_room.alarm.AlarmData
import com.example.smartpowerconnector_room.alarm.AlarmInterface
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.allRoutine.routine.TimerRoutine
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.data.allRoutine.routine.RoutinesRepository
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import com.example.smartpowerconnector_room.internet.idata.DeviceData
import com.example.smartpowerconnector_room.ui.routinescreens.clock.toClockRoutine
import com.example.smartpowerconnector_room.work.WorkRepository
import com.example.smartpowerconnector_room.work.WorkerData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * View Model to validate and insert items in the Room database.
 */
class TimerRoutineEntryViewModel(
    private val routinesRepository: RoutinesRepository,
    val itemsRepository: DeviceRepository,
    private val awsRepository: AwsRepository,
    private val workRepository: WorkRepository
    ) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var timerRoutineUiState by mutableStateOf(TimerRoutineUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(timerRoutineDetails: TimerRoutineDetails) {
        timerRoutineUiState =
            TimerRoutineUiState(timerRoutineDetails = timerRoutineDetails, isEntryValid = validateInput(timerRoutineDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveTimerRoutine() {
        if (validateInput()) {
            routinesRepository.insertTimerRoutine(timerRoutineUiState.timerRoutineDetails.toTimerRoutine())
//            val deviceData = DeviceData(
//                deviceName =timerRoutineUiState.timerRoutineDetails.deviceName ,
//                deviceDescription =timerRoutineUiState.timerRoutineDetails.deviceDescription  ,
//                deviceId =timerRoutineUiState.timerRoutineDetails.deviceId ,
//                deviceStatus =timerRoutineUiState.timerRoutineDetails.deviceStatus ,
//                deviceTime =timerRoutineUiState.timerRoutineDetails.duration.toInt()
//            )



            val workData = WorkerData(
                deviceDescription = timerRoutineUiState.timerRoutineDetails.deviceDescription,
                deviceId = timerRoutineUiState.timerRoutineDetails.deviceId ,
                deviceStatus = timerRoutineUiState.timerRoutineDetails.deviceStatus ,
                deviceName =timerRoutineUiState.timerRoutineDetails.deviceName,
                routineId = timerRoutineUiState.timerRoutineDetails.name,
                duration = timerRoutineUiState.timerRoutineDetails.duration,
            )
            workRepository.scheduleRoutine(workData)
//            alarmInterface.schedule(alarmData  =alarmData )
//            val updatedDevice = deviceData.copy(
//                deviceStatus = if (deviceData.deviceStatus == "Off") "On" else "Off"
//            )
////            awsRepository.onOffSwitch(updatedDevice)
////            val repoDevice = device.copy(
////                deviceStatus = if (currentDevice.deviceStatus == "Off") "On" else "Off"
////            )
//            deviceRepository.updateDevice(repoDevice)
            //awsRepository.onOffSwitch(updatedDevice)
        }
    }

    private fun validateInput(uiState: TimerRoutineDetails = timerRoutineUiState.timerRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()  &&status.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class TimerRoutineUiState(
    val timerRoutineDetails: TimerRoutineDetails = TimerRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class TimerRoutineDetails(
    val id: Int = 0,
    val deviceId: String = "",
    val name: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val duration: String = "",
    val status: String = "",
    val deviceName: String ="",
    val deviceStatus: String ="",
    val deviceDescription: String ="",
    )

fun TimerRoutineDetails.toTimerRoutineDetails(): TimerRoutineDetails= TimerRoutineDetails(
    id= id,
    deviceId=deviceId,
    name= name,
    startTime =startTime,
    endTime=endTime,
    duration=duration,
    status=status,
    deviceName=deviceName,
    deviceStatus=status,
    deviceDescription=deviceDescription,
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun TimerRoutineDetails.toTimerRoutine(): TimerRoutine = TimerRoutine(
    id = id,
    deviceId = deviceId,
    name = name,
    startTime = startTime,//.toDoubleOrNull() ?: 0.0,
    endTime = endTime,//.toDoubleOrNull() ?: 0.0,
    duration = duration,//.toDoubleOrNull() ?: 0.0,
    status = status//.toBooleanStrictOrNull()
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun TimerRoutine.toTimerRoutineUiState(isEntryValid: Boolean = false): TimerRoutineUiState = TimerRoutineUiState(
    timerRoutineDetails = this.toTimerRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun TimerRoutine.toTimerRoutineDetails(): TimerRoutineDetails = TimerRoutineDetails(
    id = id,
    name = name,
    deviceId = deviceId,
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    duration = duration,
    status = status
)
/*
data class AlarmDataDetails(
    val time: LocalDateTime= LocalDateTime.now(),
    val routineId: String = "",
    val device: Device = Device(
        deviceName = "",
        deviceDescription = "",
        deviceId = "",
        deviceStatus = ""
    )
)
 */