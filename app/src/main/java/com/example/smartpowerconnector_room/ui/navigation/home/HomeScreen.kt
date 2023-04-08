package com.example.smartpowerconnector_room.ui.navigation.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.internet.idata.*
import com.example.smartpowerconnector_room.ui.AppViewModelProvider
import com.example.smartpowerconnector_room.ui.navigation.NavigationDestination


object HomeScreen : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_screen
}

@Composable
fun HomeScreen(
    navigateToDeviceEntry:() -> Unit,
    navigateToDeviceUpdate: (Int)-> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
  ){
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar= {
            SPCAppTopAppBar(title = stringResource(HomeScreen.titleRes), canNavigateBack = false)
        },
        floatingActionButton = {
            FloatingActionButton(
                    onClick = navigateToDeviceEntry,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_device),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ){ innerPadding ->
        localHomeBody(
            deviceList = homeUiState.deviceList,
            onDeviceClick = navigateToDeviceUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun localHomeBody(
    deviceList: List<Device>,
    onDeviceClick: (Int) -> Unit,
    modifier: Modifier=Modifier,
    viewModel2: NetworkHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        InventoryListHeader()
        Divider()
        if(deviceList.isEmpty()){
            Text(
                text = stringResource(R.string.no_device),
                style = MaterialTheme.typography.subtitle2
            )
        } else{
            LocalInventoryList(deviceList= deviceList, onDeviceClick= { onDeviceClick(it.id) })
        }
        Divider( )
        Text(
            stringResource(R.string.network_message),
            fontSize = 24.sp,
            modifier = Modifier
        )
        Divider()
        NetworkHomeBody(awsUiState = viewModel2.awsUiState, retryAction = viewModel2::getAllDevices)
    }
}

@Composable
fun NetworkHomeBody(
    awsUiState: AwsUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    when(awsUiState) {
        is AwsUiState.Loading -> LoadingScreen(modifier)
        is AwsUiState.Success -> NetworkInventoryList(awsUiState.iDeviceList,  modifier)
        is AwsUiState.Error -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
private fun LocalInventoryList(
    deviceList: List<Device>,
    onDeviceClick: (Device) -> Unit,
    modifier: Modifier= Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(items =deviceList, key = {it.id}){device->
            InventoryDevice(device = device, onDeviceClick = onDeviceClick)
            Divider()
        }
    }
}

@Composable
fun NetworkInventoryList(
    iDeviceList: List<DeviceData>,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items( items = iDeviceList, key = {it.deviceName}){iDevice ->
            NetworkDevice(iDevice = iDevice)
            Divider()
        }
    }
}


@Composable
fun NetworkDevice(
    iDevice: DeviceData,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = iDevice.deviceName,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = iDevice.deviceId,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = iDevice.deviceStatus,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = iDevice.deviceDescription,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InventoryDevice(
    device: Device,
    onDeviceClick: (Device) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDeviceClick(device) }
            .padding(vertical = 16.dp)
    ){
        Text(
            text = device.deviceName,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = device.deviceId,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = device.deviceStatus,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = device.deviceDescription,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InventoryListHeader(modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        headerList.forEach{
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

private data class InventoryHeader(@StringRes val headerStringId: Int, val weight: Float)

private val  headerList = listOf(
    InventoryHeader(headerStringId = R.string.device, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.device_id, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.device_description, weight = 1.5f)
)
