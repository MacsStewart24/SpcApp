package com.example.smartpowerconnector_room.internet

import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.data.Devices
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "" // FIXME: You need to get URL from Rakinder

private val retrofit= Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

object SpcApi{
    val retrofitService: SpcApiService by lazy {
        retrofit.create(SpcApiService::class.java)
    }
}
// GET : retrieves data from the server
interface SpcApiService{
    @GET("device")                         // this tells the app what the end point is. In this case "device"
    suspend fun getDevice(): List<Devices> //The return type is a list of devices
}

// PUT : Replaces one piece of data that exists in the server with the data sent from the app

// POST : Sends data to be added to the server NOTE: It does not remove the existing data

// DELETE : Removes data from the server and doesn't replace it with anything