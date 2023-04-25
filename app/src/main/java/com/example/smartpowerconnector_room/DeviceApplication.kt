package com.example.smartpowerconnector_room

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.smartpowerconnector_room.data.AppContainer
import com.example.smartpowerconnector_room.data.AppDataContainer
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DeviceApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
