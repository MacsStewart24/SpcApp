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

package com.example.smartpowerconnector_room.ui.routinescreens.mixed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutine
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutineRepository
import com.example.smartpowerconnector_room.work.WorkRepository
import com.example.smartpowerconnector_room.work.WorkerData
import kotlinx.coroutines.flow.Flow

/**
 * View Model to validate and insert items in the Room database.
 */

class MixRoutineEntryViewModel(
    private val mixRoutineRepository: MixRoutineRepository,
    val itemsRepository: DeviceRepository,
    private val workRepository: WorkRepository
    ) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var mixRoutineUiState by mutableStateOf(MixRoutineUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(mixRoutineDetails: MixRoutineDetails) {
        mixRoutineUiState =
            MixRoutineUiState(mixRoutineDetails = mixRoutineDetails, isEntryValid = validateInput(mixRoutineDetails))
    }

    suspend fun getAllItemsStream(): Flow<List<Device>> {
        return itemsRepository.getAllDevicesStream()
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem(item: Device) {
        itemsRepository.insertDevice(item)
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveMixRoutine() {
        if (validateInput()) {
            mixRoutineRepository.insertMixRoutine(mixRoutineUiState.mixRoutineDetails.toMixRoutine())
            val workData = WorkerData(
                routineId = mixRoutineUiState.mixRoutineDetails.name,
                duration = mixRoutineUiState.mixRoutineDetails.duration.toString(),
                deviceDescription = mixRoutineUiState.mixRoutineDetails.deviceDescription,
                deviceId = mixRoutineUiState.mixRoutineDetails.deviceId,
                deviceStatus = mixRoutineUiState.mixRoutineDetails.status,
                deviceName = mixRoutineUiState.mixRoutineDetails.deviceName
            )
            workRepository.scheduleRoutine(workData)
            val workData2 = WorkerData(
                routineId = mixRoutineUiState.mixRoutineDetails.name,
                duration = mixRoutineUiState.mixRoutineDetails.duration2.toString(),
                deviceDescription = mixRoutineUiState.mixRoutineDetails.deviceDescription2,
                deviceId = mixRoutineUiState.mixRoutineDetails.deviceId2,
                deviceStatus = mixRoutineUiState.mixRoutineDetails.status2,
                deviceName = mixRoutineUiState.mixRoutineDetails.deviceName2
            )
            workRepository.scheduleRoutine(workData2)
        }
    }

    private fun validateInput(uiState: MixRoutineDetails = mixRoutineUiState.mixRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class MixRoutineUiState(
    val mixRoutineDetails: MixRoutineDetails = MixRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class MixRoutineDetails(
    val id: Int = 0,
    val name: String = "",
    val time: String = "",

    val deviceId: String = "",
    val deviceName:String = "",
    val deviceDescription: String = "",
    val status: String = "",
    val duration: Long = 0,

    val deviceId2: String = "",
    val deviceName2:String = "",
    val deviceDescription2: String = "",
    val status2: String = "",
    val duration2: Long= 0,

    val endTime: String = "",
    val startTime: String = "",
    val endTime2: String = "",
    val startTime2: String = "",
    val time2: String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun MixRoutineDetails.toMixRoutine(): MixRoutine = MixRoutine(
    id = id,
    deviceId = deviceId,
    name = name,
    time = time,
    status = status,//.toBooleanStrictOrNull()
    endTime = endTime,
    startTime = startTime,
    duration = duration.toString(),
    deviceId2 = deviceId2,
    time2 = time2,
    status2 = status2,//.toBooleanStrictOrNull()
    endTime2 = endTime2,
    startTime2 = startTime2,
    duration2 = duration2.toString(),

    )

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun MixRoutine.toMixRoutineUiState(isEntryValid: Boolean = false): MixRoutineUiState = MixRoutineUiState(
    mixRoutineDetails = this.toMixRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun MixRoutine.toMixRoutineDetails(): MixRoutineDetails = MixRoutineDetails(
    id = id,
    name = name,
    deviceId = deviceId,
    time = time,
    status = status,
    endTime = endTime,
    startTime = startTime,
    duration = duration.toLong(),
    deviceId2 = deviceId2,
    time2 = time2,
    status2 = status2,
    endTime2 = endTime2,
    startTime2 = startTime2,
    duration2 = duration2.toLong(),
)

