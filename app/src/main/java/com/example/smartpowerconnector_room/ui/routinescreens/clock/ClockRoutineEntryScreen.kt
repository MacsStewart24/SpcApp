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

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object ClockRoutineEntryDestination : NavigationDestination {
    override val route = "clock_routine_entry"
    override val titleRes = R.string.clock_routine_entry_title
}

@Composable
fun ClockRoutineEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: ClockRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SPCAppTopAppBar(
                title = stringResource(ClockRoutineEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ClockRoutineEntryBody(
            clockRoutineUiState = viewModel.clockRoutineUiState,
            onClockRoutineValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the clockRoutine may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveClockRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ClockRoutineEntryBody(
    clockRoutineUiState: ClockRoutineUiState,
    onClockRoutineValueChange: (ClockRoutineDetails) -> Unit,
    viewModel: ClockRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ClockRoutineInputForm(clockRoutineDetails = clockRoutineUiState.clockRoutineDetails, onClockRoutineValueChange = onClockRoutineValueChange, itemsRepository = viewModel.itemsRepository, onItemSelected = {})
        Button(
            onClick = onSaveClick,
            enabled = clockRoutineUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClockRoutineInputForm(
    clockRoutineDetails: ClockRoutineDetails,
    modifier: Modifier = Modifier,
    onClockRoutineValueChange: (ClockRoutineDetails) -> Unit = {},
    enabled: Boolean = true,
    itemsRepository: DeviceRepository,
    onItemSelected: (Device) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            text = "Name this routine:",
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        )

        OutlinedTextField(
            value = clockRoutineDetails.name,
            onValueChange = { onClockRoutineValueChange(clockRoutineDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.clock_routine_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Spacer(modifier = Modifier.size(5.dp))

        /////////////////////////////////Device Selection/////////////////////////////////////////////////////
        var items = itemsRepository.getAllDevicesStream().collectAsState(emptyList())

        var expanded3 by remember { mutableStateOf(false) }
        var selectedOption3 by remember { mutableStateOf<Device?>(null) }

        ExposedDropdownMenuBox(
            expanded = expanded3,
            onExpandedChange = { expanded3 = !expanded3 }
        ) {
            TextField(
                readOnly = true,
                value = selectedOption3?.deviceName ?: "",
                onValueChange = { },
                label = { Text("Select Device") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded3)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded3,
                onDismissRequest = { expanded3 = false }
            ) {
                items.value.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption3 = item
                            expanded3 = false
                            onItemSelected(item)
                            onClockRoutineValueChange(clockRoutineDetails.copy(
                                deviceId = item.deviceName,
                                deviceDescription = item.deviceDescription,
                                deviceName = item.deviceName
                            ))
                        }
                    ) {
                        Text(text = item.deviceName)
                    }
                }

            }
        }

        ////////////////////////END OF DEVICE SELECTION/////////////////

        val options = listOf("On", "Off")
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                label = { Text("Set Device On or Off") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            onClockRoutineValueChange(clockRoutineDetails.copy(status = selectedOptionText,
                            deviceStatus = selectedOptionText))
                        }
                    ){
                        Text(text = selectionOption)
                    }
                }
            }
        }

        //////////////////////////////////////////// END OF STATUS SELECTION////////////////////////////////////////////
        Spacer(modifier = Modifier.size(10.dp))

        // Fetching local context
        val mContext = LocalContext.current

        // Declaring and initializing a calendar
        val mCalendar = Calendar.getInstance()
        val mEndHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mEndMinute = mCalendar[Calendar.MINUTE]

// Creating mutable states to hold the selected times

        val mStartTime = LocalTime.now()
        val mEndTime = remember { mutableStateOf("") }


        val mTimePickerDialog = TimePickerDialog(
            mContext,
            { _, mHour: Int, mMinute: Int ->
                val selectedTime = "$mHour:$mMinute"
                mEndTime.value = selectedTime
                //onClockRoutineValueChange(clockRoutineDetails.copy(time = selectedTime)) // Update the time value in your repository
            }, mEndHour, mEndMinute, false
        )
        Button(onClick = { mTimePickerDialog.show() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))) {
            Text(text = "Select a time to turn on/off", color = Color.White)
        }

        Spacer(modifier = Modifier.size(20.dp))

        // Display selected start and end times
        Text(text = "Selected End Time: ${mEndTime.value}", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        @RequiresApi(Build.VERSION_CODES.S)
        fun calculateDuration(mStartTime: LocalTime, endTime: String): Long {
            return try {
                val end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))
                val duration = Duration.between(mStartTime, end)
                duration.toMillis() / 1000
//                    val hours = duration.toHours()
//                    val minutes = duration.toMinutes() % 60
//                    String.format("%d hours %d minutes", hours, minutes)
            } catch (e: DateTimeParseException) {
                -1
            }
        }
        val duration = calculateDuration(mStartTime, mEndTime.value)
//        val durationInSeconds = duration.toSec
        onClockRoutineValueChange(clockRoutineDetails.copy(duration = duration))

    }
}


