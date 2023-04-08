package com.example.smartpowerconnector_room.ui.device

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inventory.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object AddDeviceDestination : NavigationDestination{
    override val route = "add_device"
    override val titleRes = R.string.add_device_screen
}

@Composable
fun AddDeviceScreen(
    navigateBack:() -> Unit,
    onNavigateUp:() -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: AddDeviceViewModel = viewModel(factory= AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar ={
            SPCAppTopAppBar(title = stringResource(AddDeviceDestination.titleRes), canNavigateBack = canNavigateBack, navigateUp = onNavigateUp)
        }
    ) {innerPadding ->
        DeviceEntryBody(
            modifier = modifier.padding(innerPadding),
            deviceUiState = viewModel.deviceUiState,
            onDeviceValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch  {
                    viewModel.saveDevice()
                    navigateBack()
                }
            },
        )
    }
}

@Composable
fun DeviceEntryBody(
    deviceUiState: DeviceUiState,
    onDeviceValueChange: (DeviceDetails)-> Unit,
    onSaveClick: ()-> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        DeviceInputForm(deviceDetails = deviceUiState.DeviceDetails, onValueChange = onDeviceValueChange)
        Button(
            onClick = onSaveClick,
            enabled = deviceUiState.validEntry,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun DeviceInputForm(
    deviceDetails: DeviceDetails,
    modifier: Modifier = Modifier,
    onValueChange: (DeviceDetails)->Unit={},
    enabled:Boolean = true
){
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = deviceDetails.deviceName,
            onValueChange = { onValueChange(deviceDetails.copy(deviceName = it)) },
            label = { Text(stringResource(R.string.device_name)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = deviceDetails.deviceId,
            onValueChange = { onValueChange(deviceDetails.copy(deviceId = it)) },
            label = { Text(stringResource(R.string.device_id)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = deviceDetails.deviceStatus,
            onValueChange = { onValueChange(deviceDetails.copy(deviceStatus = deviceDetails.deviceStatus)) },
            label = { Text(stringResource(R.string.device_status)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = deviceDetails.deviceDescription,
            onValueChange = { onValueChange(deviceDetails.copy(deviceDescription = it)) },
            label = { Text(stringResource(R.string.device_description)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}