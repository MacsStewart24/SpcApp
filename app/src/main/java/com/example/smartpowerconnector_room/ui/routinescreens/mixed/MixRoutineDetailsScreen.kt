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

package com.example.inventory.ui.routines

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
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineDetailsUiState
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineDetailsViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineEntryViewModel
import kotlinx.coroutines.launch

object MixRoutineDetailsDestination : NavigationDestination {
    override val route = "mix_routine_details"
    override val titleRes = R.string.mix_routine_details_title
    const val mixRoutineIdArg = "mixRoutineId"
    val routeWithArgs = "$route/{$mixRoutineIdArg}"
}

@Composable
fun MixRoutineDetailsScreen(
    navigateToEditMixRoutine: (Int) -> Unit,
    navigateBack: () -> Unit,
    //navigateToMixRoutineEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MixRoutineDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SPCAppTopAppBar(
                title = stringResource(MixRoutineDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditMixRoutine(uiState.value.mixRoutineDetails.id) },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_mix_routine_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        MixRoutineDetailsBody(

            mixRoutineDetailsUiState = uiState.value,

            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the mixRoutine may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteMixRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun MixRoutineDetailsBody(
    mixRoutineDetailsUiState: MixRoutineDetailsUiState,
    viewModel: MixRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
        //MixRoutineInputForm(mixRoutineDetails = mixRoutineDetailsUiState.mixRoutineDetails, enabled = false, itemsRepository = viewModel.itemsRepository, onItemSelected = {})

        Text(
            text = "Routine Name: ${mixRoutineDetailsUiState.mixRoutineDetails.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Primary Device: ${mixRoutineDetailsUiState.mixRoutineDetails.deviceId}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Primary Device Status: ${mixRoutineDetailsUiState.mixRoutineDetails.status}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Duration: ${mixRoutineDetailsUiState.mixRoutineDetails.duration}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Time: ${mixRoutineDetailsUiState.mixRoutineDetails.time}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Secondary Device: ${mixRoutineDetailsUiState.mixRoutineDetails.deviceId2}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Secondary Device Status: ${mixRoutineDetailsUiState.mixRoutineDetails.status2}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Duration: ${mixRoutineDetailsUiState.mixRoutineDetails.duration2}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Time: ${mixRoutineDetailsUiState.mixRoutineDetails.time2}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
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

