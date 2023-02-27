package com.example.smartpowerconnector_room

import android.app.Application
import com.example.smartpowerconnector_room.data.AppContainer
import com.example.smartpowerconnector_room.data.AppDataContainer

class DeviceApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
