
package com.example.smartpowerconnector_room.alarm.data

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.core.app.NotificationCompat
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.internet.idata.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
import javax.inject.Inject

//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
class AlarmReceiver: BroadcastReceiver(){
    val awsApiService: AwsApiService = AwsApiService.instance

    override fun onReceive(context: Context?, intent: Intent?) {
        val status = intent?.getStringExtra("deviceStatus")?: return
        val deviceName = intent?.getStringExtra("deviceName")?: return
        val description = intent?.getStringExtra("description")?: return
        val id = intent?.getStringExtra("deviceId")?: return



        val RName = intent?.getStringExtra("RName")?: return
        println("RName: $RName")

    


        val deviceData = DeviceData(
            deviceName = deviceName,
            deviceStatus = status,
            deviceId = id,
            deviceDescription = description,
            //deviceTime = 0
        )

        awsApiService.updateDevice(deviceData.deviceName,deviceData)
    }
}
