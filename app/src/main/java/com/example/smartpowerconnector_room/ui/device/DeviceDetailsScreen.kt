package com.example.smartpowerconnector_room.ui.device

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutine
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutine
import com.example.smartpowerconnector_room.data.allRoutine.routine.TimerRoutine
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutine
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

// Need to add timer and routine option to this screen.

object DeviceDetailsDestination : NavigationDestination{
    override val route = "device_details"
    override val titleRes: Int = R.string.device_details
    const val deviceIDArg = "deviceID"
    val routeWithArgs = "$route/{$deviceIDArg}"
}

@Composable
fun DeviceDetailScreen(
    navigateToEditDevice: (Int)-> Unit,

    navigateToIntermediateScreen: () -> Unit,
    navigateToTimerRoutineUpdate: (Int) -> Unit,
    navigateToClockRoutineUpdate: (Int) -> Unit,
    navigateToMultiRoutineUpdate: (Int) -> Unit,
    navigateToMixRoutineUpdate: (Int) -> Unit,

    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeviceDetailsViewModel = viewModel(factory= AppViewModelProvider.Factory),
){
    val uiState = viewModel.uiState.collectAsState()
    val itemUiState2 by viewModel.itemUiState2.collectAsState()
    val itemUiState3 by viewModel.itemUiState3.collectAsState()
    val itemUiState4 by viewModel.itemUiState4.collectAsState()
    val itemUiState5 by viewModel.itemUiState5.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SPCAppTopAppBar(
                title = stringResource(DeviceDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
                )
            },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigateToEditDevice(uiState.value.deviceDetails.id) },
                    modifier = Modifier.navigationBarsPadding()
                    ) {
                    Icon(imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_device),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
    ){ innerPadding ->
        DeviceDetailBody(
            deviceDetailsUiState = uiState.value,
            onChangeStatus = {
                coroutineScope.launch {
                    viewModel.changeStatus()
                    navigateBack()
                }
            },

            onAddARoutine = navigateToIntermediateScreen,
            timerRoutineList = itemUiState2.timerRoutineList,
            onTimerRoutineClick = navigateToTimerRoutineUpdate,
            clockRoutineList = itemUiState3.clockRoutineList,
            onClockRoutineClick = navigateToClockRoutineUpdate,
            multiRoutineList = itemUiState4.multiRoutineList,
            onMultiRoutineClick = navigateToMultiRoutineUpdate,
            mixRoutineList = itemUiState5.mixRoutineList,
            onMixRoutineClick = navigateToMixRoutineUpdate,

            onDelete = {
                coroutineScope.launch{
                    viewModel.deleteDevice()
                    navigateBack()
                    }
                },
            modifier = modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun DeviceDetailBody(
    deviceDetailsUiState: DeviceDetailsUiState,
    onChangeStatus: () -> Unit,

    onAddARoutine: () -> Unit,

    timerRoutineList: List<TimerRoutine>,
    onTimerRoutineClick: (Int) -> Unit,

    clockRoutineList: List<ClockRoutine>,
    onClockRoutineClick: (Int) -> Unit,

    multiRoutineList: List<MultiRoutine>,
    onMultiRoutineClick: (Int) -> Unit,

    mixRoutineList: List<MixRoutine>,
    onMixRoutineClick: (Int) -> Unit,

    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        DeviceInputForm(deviceDetails = deviceDetailsUiState.deviceDetails, enabled = false)
        OutlinedButton(
            onClick = { onChangeStatus() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Change Status")
        }
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
                onDeleteCancel = { deleteConfirmationRequired = false })
        }
        Divider()

        Button(
            onClick = onAddARoutine,
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text(stringResource(R.string.add_a_routine))
        }

        TimerRoutineListHeader()
        Divider()

        if (timerRoutineList.isEmpty()) {
            Text(
                text = ("No Timer Routines"),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            TimerRoutineList(
                timerRoutineList = timerRoutineList,
                onTimerRoutineClick = { onTimerRoutineClick(it.id) })
        }
        if (clockRoutineList.isEmpty()) {
            Text(
                text = ("No Clock Routines"),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            ClockRoutineList(
                clockRoutineList = clockRoutineList,
                onClockRoutineClick = { onClockRoutineClick(it.id) })
        }
        if (multiRoutineList.isEmpty()) {
            Text(
                text = ("No Conditional Routines"),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            MultiRoutineList(
                multiRoutineList = multiRoutineList,
                onMultiRoutineClick = { onMultiRoutineClick(it.id) })
        }
        if (mixRoutineList.isEmpty()) {
            Text(
                text = ("No Mix Routines"),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            MixRoutineList(
                mixRoutineList = mixRoutineList,
                onMixRoutineClick = { onMixRoutineClick(it.id) })
        }
    }
}

private val headerList = listOf(
    TimerRoutineHeader(headerStringId = R.string.routine, weight = 1.5f),
)

@Composable
private fun TimerRoutineList(
    timerRoutineList: List<TimerRoutine>,
    onTimerRoutineClick: (TimerRoutine) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = timerRoutineList, key = { it.id }) { timerRoutine ->
            TimerRoutine(timerRoutine = timerRoutine, onTimerRoutineClick = onTimerRoutineClick)
            Divider()
        }
    }
}

@Composable
private fun TimerRoutineListHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun TimerRoutine(
    timerRoutine: TimerRoutine,
    onTimerRoutineClick: (TimerRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onTimerRoutineClick(timerRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = timerRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = timerRoutine.duration,
            modifier = Modifier.weight(1.0f)
        )
        Text(text = timerRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

private data class TimerRoutineHeader(@StringRes val headerStringId: Int, val weight: Float)

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = { },
        title = {Text(stringResource(R.string.attention))},
        text = { Text(stringResource(R.string.sure))},
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick =  onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm){
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}


@Composable
private fun ClockRoutineList(
    clockRoutineList: List<ClockRoutine>,
    onClockRoutineClick: (ClockRoutine) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = clockRoutineList, key = { it.id }) { clockRoutine ->
            ClockRoutine(clockRoutine = clockRoutine, onClockRoutineClick = onClockRoutineClick)
            Divider()
        }
    }
}

@Composable
private fun ClockRoutine(
    clockRoutine: ClockRoutine,
    onClockRoutineClick: (ClockRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onClockRoutineClick(clockRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = clockRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
//        Text(
//            text = clockRoutine.time,
//            modifier = Modifier.weight(1.0f)
//        )
        Text(text = clockRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

@Composable
private fun MultiRoutineList(
    multiRoutineList: List<MultiRoutine>,
    onMultiRoutineClick: (MultiRoutine) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = multiRoutineList, key = { it.id }) { multiRoutine ->
            MultiRoutine(multiRoutine = multiRoutine, onMultiRoutineClick = onMultiRoutineClick)
            Divider()
        }
    }
}



@Composable
private fun MultiRoutine(
    multiRoutine: MultiRoutine,
    onMultiRoutineClick: (MultiRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onMultiRoutineClick(multiRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = multiRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
//        Text(
//            text = clockRoutine.time,
//            modifier = Modifier.weight(1.0f)
//        )
        Text(text = multiRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

@Composable
private fun MixRoutineList(
    mixRoutineList: List<MixRoutine>,
    onMixRoutineClick: (MixRoutine) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = mixRoutineList, key = { it.id }) { mixRoutine ->
            MixRoutine(mixRoutine = mixRoutine, onMixRoutineClick = onMixRoutineClick)
            Divider()
        }
    }
}



@Composable
private fun MixRoutine(
    mixRoutine: MixRoutine,
    onMixRoutineClick: (MixRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onMixRoutineClick(mixRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = mixRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
//        Text(
//            text = clockRoutine.time,
//            modifier = Modifier.weight(1.0f)
//        )
        Text(text = mixRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

