package com.example.smartpowerconnector_room.internet.idata

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.Outline
import com.example.inventory.R
import com.example.smartpowerconnector_room.DeviceApplication
import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.DeviceRepository
import com.example.smartpowerconnector_room.ui.device.DeviceDetails
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.io.IOException
import javax.inject.Inject


interface AwsRepository {
    suspend fun getAllDevices(): List<DeviceData>
    suspend fun onOffSwitch(deviceData: DeviceData) : Response<DeviceData>
    fun updateDevice(deviceData: DeviceData) : Response<DeviceData>
    suspend fun deleteDevice(deviceData: DeviceData): Response<DeviceData>
}

class NetworkDeviceRepository(
    private val awsApiService: AwsApiService,
):AwsRepository{

    override suspend fun getAllDevices(): List<DeviceData> = awsApiService.getAllDevices()

    override suspend fun onOffSwitch(deviceData: DeviceData): Response<DeviceData> {
        return awsApiService.onOffSwitch(deviceData.deviceName, deviceData)
    }

    override suspend fun deleteDevice(deviceData: DeviceData): Response<DeviceData> {
        return awsApiService.deleteDevice(deviceData.deviceName)
    }

    override fun updateDevice(deviceData: DeviceData): Response<DeviceData> {
           return awsApiService.updateDevice(deviceData.deviceName, deviceData)
    }
}

interface UseRepository{
    suspend fun getUsages(): List<Usage>
}

class NetworkUsagesRepository(private val awsApi: AwsApi): UseRepository{
    override suspend fun getUsages(): List<Usage> = awsApi.getUsages()
}