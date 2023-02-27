package com.example.smartpowerconnector_room.ui.device

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Need to add timer and routine option to this screen.

object DeviceDetailsDestination : NavigationDestination{
    override val route = "device_details"
    override val titleRes: Int = R.string.device_details
    const val deviceIDArg = "deviceId"
    val routeWithArgs = "$route/{$deviceIDArg}"
}

@Composable
fun DeviceDetailScreen(
    navigateToEditDevice: (Int)-> Unit,
    navigateBack: ()->Unit,
    modifier: Modifier = Modifier,
    viewModel: DeviceDetailsViewModel = viewModel(factory= AppViewModelProvider.Factory)
){
    val uiState = viewModel.uiState.collectAsState()
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
                    onClick = {navigateToEditDevice(uiState.value.deviceDetails.id)},
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
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        DeviceInputForm(deviceDetails = deviceDetailsUiState.deviceDetails, enabled = false)
        OutlinedButton(
            onClick = {deleteConfirmationRequired = true},
            modifier= Modifier.fillMaxWidth()
        ){
            Text(stringResource(R.string.delete))
        }
        if(deleteConfirmationRequired){
            DeleteConfirmationDialog(
                onDeleteConfirm = { deleteConfirmationRequired = false
                                    onDelete()},
                onDeleteCancel = { deleteConfirmationRequired = false })
        }
    }
}

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