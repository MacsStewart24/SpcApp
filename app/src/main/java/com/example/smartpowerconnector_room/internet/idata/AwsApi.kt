package com.example.smartpowerconnector_room.internet.idata

import com.google.gson.Gson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path



interface AwsApiService{
    @GET("items")
    suspend fun getAllDevices(): List<DeviceData>



    @PUT("items/{id}")
    suspend fun onOffSwitch(@Path("id") id: String, @Body deviceData: DeviceData): Response<DeviceData>

    @DELETE("items/{id}")
    suspend fun deleteDevice(@Path("id") id: String): Response<DeviceData>

    @PUT("items/{id}")
    fun updateDevice(@Path("id") id: String, @Body deviceData: DeviceData): Response<DeviceData>

    //@PATCH("items/{id}")
    //fun patchDevice(@Path("id") id: String, @ParameterName() deviceData: DeviceData): Response<DeviceData>



    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("https://aaeh7rnpl1.execute-api.us-east-1.amazonaws.com/")
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(AwsApiService::class.java)
        }
    }
}

interface AwsApi{
    @GET("displayfunction")
    suspend fun getUsages(): List<Usage>
}
