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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ClockRoutineDetailsDestination : NavigationDestination {
    override val route = "clock_routine_details"
    override val titleRes = R.string.clock_routine_details_title
    const val clockRoutineIdArg = "clockRoutineId"
    val routeWithArgs = "$route/{$clockRoutineIdArg}"
}

@Composable
fun ClockRoutineDetailsScreen(
    navigateToEditClockRoutine: (Int) -> Unit,
    navigateBack: () -> Unit,
    //navigateToClockRoutineEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClockRoutineDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SPCAppTopAppBar(
                title = stringResource(ClockRoutineDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditClockRoutine(uiState.value.clockRoutineDetails.id) },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_clock_routine_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        ClockRoutineDetailsBody(
            /*clockRoutineUiState = viewModel.clockRoutineUiState,
            onRoutineValueChange = viewModel::updateUiState,
            onTimeSet = {coroutineScope.launch {
                viewModel.saveRoutine()
                navigateBack()
            }*/

            clockRoutineDetailsUiState = uiState.value,
            //onSellClockRoutine = { viewModel.reduceQuantityByOne() },
            //onAddClockRoutine = {navigateToClockRoutineEntry()},
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the clockRoutine may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteClockRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ClockRoutineDetailsBody(
    clockRoutineDetailsUiState: ClockRoutineDetailsUiState,
    //onSellClockRoutine: () -> Unit,
    //onAddClockRoutine: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier

) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        //ClockRoutineInputForm(clockRoutineDetails = clockRoutineDetailsUiState.clockRoutineDetails, enabled = false)
        Text(
            text = "Routine ID: ${clockRoutineDetailsUiState.clockRoutineDetails.id}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Device Name: ${clockRoutineDetailsUiState.clockRoutineDetails.deviceId}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Name: ${clockRoutineDetailsUiState.clockRoutineDetails.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Time: ${clockRoutineDetailsUiState.clockRoutineDetails.duration}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Status: ${clockRoutineDetailsUiState.clockRoutineDetails.status}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}

