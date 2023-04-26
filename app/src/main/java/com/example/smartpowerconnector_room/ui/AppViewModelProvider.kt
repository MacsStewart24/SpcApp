package com.example.smartpowerconnector_room.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.smartpowerconnector_room.DeviceApplication
import com.example.smartpowerconnector_room.internet.idata.NetworkHomeViewModel
import com.example.smartpowerconnector_room.ui.device.AddDeviceViewModel
import com.example.smartpowerconnector_room.ui.device.DeviceDetailsViewModel
import com.example.smartpowerconnector_room.ui.device.EditDeviceViewModel
import com.example.smartpowerconnector_room.home.HomeViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.clock.ClockRoutineEditViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.clock.ClockRoutineEntryViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineEditViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineEntryViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.multi.MultiRoutineEditViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.multi.MultiRoutineEntryViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.routine.TimerRoutineDetailsViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.routine.TimerRoutineEditViewModel
import com.example.smartpowerconnector_room.ui.routinescreens.routine.TimerRoutineEntryViewModel
import com.example.smartpowerconnector_room.usage.UsageViewModel


object AppViewModelProvider {
    val Factory= viewModelFactory {

        initializer {
            EditDeviceViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.deviceRepository,
                DeviceApplication().container.awsRepository
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
                DeviceApplication().container.awsRepository,
                DeviceApplication().container.routinesRepository,
                DeviceApplication().container.clockRoutineRepository,
                DeviceApplication().container.multiRoutineRepository,
                DeviceApplication().container.mixRoutineRepository
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
        initializer {
            UsageViewModel(
            DeviceApplication().container.useRepository
//                    DeviceApplication().container.awsRepository
            )
        }
        //////////////////////////Timer Routine/////////////////////////////////

        // Initializer for TimerRoutineEntryViewModel
        initializer {
            TimerRoutineEntryViewModel(
                DeviceApplication().container.routinesRepository,
                DeviceApplication().container.deviceRepository,
                DeviceApplication().container.awsRepository,
                DeviceApplication().container.workRepository
                )
        }

        initializer {
            TimerRoutineEditViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.routinesRepository
            )
        }
        initializer {
            TimerRoutineDetailsViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.routinesRepository
            )
        }

        ///////////////////////////////////Clock Routine///////////////////////////
        initializer {
            ClockRoutineEntryViewModel(
                DeviceApplication().container.clockRoutineRepository,
                DeviceApplication().container.deviceRepository,
                DeviceApplication().container.workRepository
            )
        }
        initializer {
            ClockRoutineEditViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.clockRoutineRepository
            )

        }

//        initializer {
//            ClockRoutineDetailsViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.clockRoutineRepository
//            )
//        }

        //////////////////////////Multi Routine/////////////////////////
        initializer {
            MultiRoutineEntryViewModel(
                DeviceApplication().container.multiRoutineRepository,
                DeviceApplication().container.deviceRepository,
                DeviceApplication().container.awsRepository
            )
        }
        initializer {
            MultiRoutineEditViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.multiRoutineRepository
            )
        }
//        initializer {
//            MultiRoutineDetailsViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.multiRoutineRepository
//            )
//        }

        /////////////////////////Mix Routine//////////////////////////////
        initializer {
            MixRoutineEntryViewModel(
                DeviceApplication().container.mixRoutineRepository,
                DeviceApplication().container.deviceRepository,
                DeviceApplication().container.workRepository
            )
        }
        initializer {
            MixRoutineEditViewModel(
                this.createSavedStateHandle(),
                DeviceApplication().container.mixRoutineRepository
            )
        }

    }
}


fun CreationExtras.DeviceApplication(): DeviceApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DeviceApplication)