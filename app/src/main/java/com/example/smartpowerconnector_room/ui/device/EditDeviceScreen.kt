package com.example.smartpowerconnector_room.ui.device

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch



object DeviceEditDestination : NavigationDestination{
    override val route = "device_edit"
    override val titleRes= R.string.edit_device
    const val deviceIDArg = "deviceID"
    val routeWithArgs = "$route/{$deviceIDArg}"
}


@Composable
fun EditDeviceScreen(
    navigateBack: ()->Unit,
    onNavigateUp: ()-> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditDeviceViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SPCAppTopAppBar(
                title = stringResource(DeviceEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
                )
        }
    ) {innerPadding->
        DeviceEntryBody(
            deviceUiState = viewModel.deviceUiState,
            onDeviceValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch{
                    viewModel.updateDevice()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

