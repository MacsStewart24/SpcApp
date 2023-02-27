package com.example.smartpowerconnector_room.data

import android.content.Context


interface AppContainer{
    val deviceRepository: DeviceRepository
}

class AppDataContainer(private val context: Context): AppContainer{
    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(DeviceDatabase.getDatabase(context).deviceDao())
    }
}