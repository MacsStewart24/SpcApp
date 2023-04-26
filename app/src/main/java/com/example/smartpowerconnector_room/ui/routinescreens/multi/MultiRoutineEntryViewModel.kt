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

package com.example.smartpowerconnector_room.ui.routinescreens.multi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutine
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutineRepository
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import com.example.smartpowerconnector_room.internet.idata.DeviceData
import com.example.smartpowerconnector_room.ui.device.toDeviceData

/**
 * View Model to validate and insert items in the Room database.
 */
class MultiRoutineEntryViewModel(
    private val multiRoutineRepository: MultiRoutineRepository,
    val itemsRepository: DeviceRepository,
    private val awsRepository: AwsRepository
    ) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var multiRoutineUiState by mutableStateOf(MultiRoutineUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(multiRoutineDetails: MultiRoutineDetails) {
        multiRoutineUiState =
            MultiRoutineUiState(multiRoutineDetails = multiRoutineDetails, isEntryValid = validateInput(multiRoutineDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveMultiRoutine() {
        if (validateInput()) {
            multiRoutineRepository.insertMultiRoutine(multiRoutineUiState.multiRoutineDetails.toMultiRoutine())
            val currentDevice1 = DeviceData(
                deviceDescription = multiRoutineUiState.multiRoutineDetails.deviceDescription,
                deviceName = multiRoutineUiState.multiRoutineDetails.deviceName,
                deviceStatus = multiRoutineUiState.multiRoutineDetails.status,
                deviceId = multiRoutineUiState.multiRoutineDetails.deviceId,
            )
            awsRepository.onOffSwitch(currentDevice1)
            val currentDevice2 = DeviceData(
                deviceDescription = multiRoutineUiState.multiRoutineDetails.deviceDescription2,
                deviceName = multiRoutineUiState.multiRoutineDetails.deviceName2,
                deviceStatus = multiRoutineUiState.multiRoutineDetails.status2,
                deviceId = multiRoutineUiState.multiRoutineDetails.deviceId2,
            )
            awsRepository.onOffSwitch(currentDevice2)
        }
    }

    private fun validateInput(uiState: MultiRoutineDetails = multiRoutineUiState.multiRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && /*time.isNotBlank() &&*/ status.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class MultiRoutineUiState(
    val multiRoutineDetails: MultiRoutineDetails = MultiRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class MultiRoutineDetails(
    val id: Int = 0,
    val name: String = "",
    val deviceId: String = "",
    val deviceName:String = "",
    val deviceDescription :String = "",

    val deviceId2: String = "",
    val deviceName2:String = "",
    val deviceDescription2: String = "",
    val status: String = "",
    val status2: String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun MultiRoutineDetails.toMultiRoutine(): MultiRoutine = MultiRoutine(
    id = id,
    name = name,
    deviceId = deviceId,
    status = status,
    deviceId2 = deviceId2,
    status2 = status2
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun MultiRoutine.toMultiRoutineUiState(isEntryValid: Boolean = false): MultiRoutineUiState = MultiRoutineUiState(
    multiRoutineDetails = this.toMultiRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun MultiRoutine.toMultiRoutineDetails(): MultiRoutineDetails = MultiRoutineDetails(
    id = id,
    deviceId= deviceId,
    name = name,
    status = status,
    deviceId2= deviceId2,
    status2 = status2
)

