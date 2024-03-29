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

package com.example.smartpowerconnector_room.ui.routinescreens.clock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpowerconnector_room.alarm.AlarmData
import com.example.smartpowerconnector_room.alarm.AlarmInterface
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutine
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutineRepository
import com.example.smartpowerconnector_room.ui.routinescreens.routine.TimerRoutineDetails
import com.example.smartpowerconnector_room.work.RoutineWorker
import com.example.smartpowerconnector_room.work.WorkRepository
import com.example.smartpowerconnector_room.work.WorkerData
import java.time.LocalDateTime

/**
 * View Model to validate and insert items in the Room database.
 */
class ClockRoutineEntryViewModel(
    private val clockRoutineRepository: ClockRoutineRepository,
    val itemsRepository: DeviceRepository,
    private val workRepository: WorkRepository
    ) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var clockRoutineUiState by mutableStateOf(ClockRoutineUiState())
        private set
    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(clockRoutineDetails: ClockRoutineDetails) {
        clockRoutineUiState =
            ClockRoutineUiState(clockRoutineDetails = clockRoutineDetails, isEntryValid = validateInput(clockRoutineDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveClockRoutine() {
        if (validateInput()) {
            clockRoutineRepository.insertClockRoutine(clockRoutineUiState.clockRoutineDetails.toClockRoutine())
            val workData = WorkerData(
                routineId = clockRoutineUiState.clockRoutineDetails.name,
                duration = clockRoutineUiState.clockRoutineDetails.duration.toString(),
                deviceDescription = clockRoutineUiState.clockRoutineDetails.deviceDescription,
                deviceId = clockRoutineUiState.clockRoutineDetails.deviceId ,
                deviceStatus = clockRoutineUiState.clockRoutineDetails.deviceStatus ,
                deviceName = clockRoutineUiState.clockRoutineDetails.deviceName
            )
            workRepository.scheduleRoutine(workData)
        }
    }

    private fun validateInput(uiState: ClockRoutineDetails = clockRoutineUiState.clockRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && status.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ClockRoutineUiState(
    val clockRoutineDetails: ClockRoutineDetails = ClockRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class ClockRoutineDetails(
    val id: Int = 0,
    val name: String = "",
    val duration: Long =0,
    val status: String = "",
    val deviceName: String ="",
    val deviceId: String = "",
    val deviceStatus: String ="",
    val deviceDescription: String ="",
)


/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun ClockRoutineDetails.toClockRoutine(): ClockRoutine = ClockRoutine(
    id = id,
    deviceId = deviceId,
    name = name,
    duration = duration.toString(),//.toDoubleOrNull() ?: 0.0,
    status = status//.toBooleanStrictOrNull()
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun ClockRoutine.toClockRoutineUiState(isEntryValid: Boolean = false): ClockRoutineUiState = ClockRoutineUiState(
    clockRoutineDetails = this.toClockRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun ClockRoutine.toClockRoutineDetails(): ClockRoutineDetails = ClockRoutineDetails(
    id = id,
    deviceId = deviceId,
    name = name,
    duration = duration.toLong(),//.toString(),
    status = status
)
