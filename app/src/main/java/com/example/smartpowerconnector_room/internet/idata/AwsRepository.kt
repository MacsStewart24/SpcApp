package com.example.smartpowerconnector_room.internet.idata

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.io.IOException


interface AwsRepository {
    suspend fun getAllDevices(): List<DeviceData>
    suspend fun onOffSwitch(/*string: String,*/ deviceData: DeviceData): Call<DeviceData>
}

class NetworkDeviceRepository(
    private val awsApiService: AwsApiService
):AwsRepository{
    override suspend fun getAllDevices(): List<DeviceData> = awsApiService.getAllDevices()

    override suspend fun onOffSwitch(deviceData: DeviceData): Call<DeviceData> {
        try {
            awsApiService.onOffSwitch(deviceData).enqueue(object : Callback<DeviceData> {
                override fun onResponse(call: Call<DeviceData>, response: Response<DeviceData>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            return Unit
                        }
                    } else {
                        // handle error case here
                        val errorBody = response.errorBody()?.string()
                        Log.e("DeviceDetailsViewModel", "Error: $errorBody")
                    }
                }

                override fun onFailure(call: Call<DeviceData>, t: Throwable) {
                    // handle network error here
                    Log.e("DeviceDetailsViewModel", "Network error: ${t.message}")
                }
            })
        } catch (e: IOException) {
            // handle network error here
            Log.e("DeviceDetailsViewModel", "Network error: ${e.message}")
        }
        return awsApiService.onOffSwitch(deviceData)
    }
}
