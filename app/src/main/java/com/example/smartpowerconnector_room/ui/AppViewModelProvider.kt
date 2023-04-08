package com.example.smartpowerconnector_room.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.smartpowerconnector_room.DeviceApplication
import com.example.smartpowerconnector_room.internet.idata.DeviceData
import com.example.smartpowerconnector_room.internet.idata.NetworkHomeViewModel
import com.example.smartpowerconnector_room.ui.device.AddDeviceViewModel
import com.example.smartpowerconnector_room.ui.device.DeviceDetailScreen
import com.example.smartpowerconnector_room.ui.device.DeviceDetailsViewModel
import com.example.smartpowerconnector_room.ui.device.EditDeviceViewModel
import com.example.smartpowerconnector_room.ui.navigation.home.HomeViewModel


object AppViewModelProvider {
    val Factory= viewModelFactory {

        initializer {
            EditDeviceViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.deviceRepository
            )
        }

        initializer {
            AddDeviceViewModel(
                DeviceApplication().container.deviceRepository
            )
        }
        initializer {
            DeviceDetailsViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.deviceRepository,
                DeviceApplication().container.awsRepository
            )
        }

        initializer {
            HomeViewModel(
                DeviceApplication().container.deviceRepository
            )
        }
        initializer {
            NetworkHomeViewModel(
                DeviceApplication().container.awsRepository
            )
        }
    }
}


fun CreationExtras.DeviceApplication(): DeviceApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DeviceApplication)