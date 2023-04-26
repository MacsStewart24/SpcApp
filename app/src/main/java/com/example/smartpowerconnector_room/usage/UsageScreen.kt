package com.example.smartpowerconnector_room.usage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.home.HomeScreen
import com.example.smartpowerconnector_room.internet.idata.*
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.device.AddDeviceDestination
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination

object UsageDetailsDestination : NavigationDestination {
    override val route = "usage_details"
    override val titleRes = R.string.usage_title
}


@Composable
fun UsageScreen(
    navigateBack:() -> Unit,
    onNavigateUp:() -> Unit,

    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel2: UsageViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SPCAppTopAppBar(
                title = stringResource(AddDeviceDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp)
                },
    ) {
        Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colors.background,
            ) {
            locHomeBody(
                modifier = Modifier
            )
            }
        }
    }

@Composable
fun locHomeBody(
    modifier: Modifier=Modifier,
    viewModel2: UsageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NetworkUsageHomeBody(
            useUiState = viewModel2.useUiState,
            retryAction = viewModel2::getUsages
        )

    }
}

@Composable
fun NetworkUsageHomeBody(
    useUiState: UseUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
){
    when(useUiState) {
        is UseUiState.Loading -> LoadingScreen(modifier)
        is UseUiState.Success -> NetworkUsageList(useUiState.iDeviceList, modifier)
        is UseUiState.Error -> ErrorScreen(retryAction, modifier)
    }
}
@Composable
fun NetworkUsageList(
    iDeviceList: List<Usage>,
//    iDeviceList: List<DeviceData>,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items( items = iDeviceList, key = {it.timestamp}){iDevice ->
            NetworkDevice(iDevice = iDevice)
            Divider()
        }
    }
}

@Composable
fun NetworkDevice(
    iDevice: Usage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = iDevice.boardID,
//            text = iDevice.deviceStatus,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = iDevice.current.toString(),
//            text = iDevice.deviceName,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = iDevice.status,
//            text = iDevice.deviceDescription,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = iDevice.voltage.toString(),
//            text = iDevice.deviceId,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
    }
}