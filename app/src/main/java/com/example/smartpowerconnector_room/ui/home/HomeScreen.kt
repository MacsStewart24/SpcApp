package com.example.smartpowerconnector_room.ui.home

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.smartpowerconnector_room.SPCAppTopAppBar
import com.example.smartpowerconnector_room.data.Device
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
  viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar= {
            SPCAppTopAppBar(title = stringResource(HomeScreen.titleRes), canNavigateBack = false)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToDeviceEntry, modifier = Modifier.navigationBarsPadding() ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_device),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ){ innerPadding ->
        HomeBody(deviceList = homeUiState.deviceList, onDeviceClick = navigateToDeviceUpdate, modifier = modifier.padding(innerPadding))
    }
}

@Composable
private fun HomeBody(
    deviceList: List<Device>,
    onDeviceClick: (Int) -> Unit,
    modifier: Modifier=Modifier
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
            InventoryList(deviceList= deviceList, onDeviceClick= { onDeviceClick(it.id) })
        }
    }
}

@Composable
private fun InventoryList(
    deviceList: List<Device>,
    onDeviceClick: (Device) -> Unit,
    modifier: Modifier= Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(items =deviceList, key = {it.deviceId}){device->
            InventoryDevice(device = device, onDeviceClick = onDeviceClick)
            Divider()
        }
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


@Composable
private fun InventoryDevice(
    device: Device,
    onDeviceClick: (Device)->Unit,
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
            text = device.deviceDescription,
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )

    }
}

private data class InventoryHeader(@StringRes val headerStringId: Int, val weight: Float)

private val  headerList = listOf(
    InventoryHeader(headerStringId = R.string.device, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.device_id, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.device_description, weight = 1.5f)
)
