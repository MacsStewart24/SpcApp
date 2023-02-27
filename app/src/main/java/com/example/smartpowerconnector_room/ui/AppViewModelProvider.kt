package com.example.smartpowerconnector_room.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.smartpowerconnector_room.DeviceApplication
import com.example.smartpowerconnector_room.ui.device.AddDeviceViewModel
import com.example.smartpowerconnector_room.ui.device.EditDeviceViewModel
import com.example.smartpowerconnector_room.ui.home.HomeViewModel


object AppViewModelProvider {
    val Factory= viewModelFactory {
        initializer {
            AddDeviceViewModel(
                DeviceApplication().container.deviceRepository
            )
        }
        initializer {
            EditDeviceViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.deviceRepository
            )
        }
        initializer {
            HomeViewModel(
                DeviceApplication().container.deviceRepository
            )
        }
    }
}


fun CreationExtras.DeviceApplication(): DeviceApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DeviceApplication)