package com.example.smartpowerconnector_room.internet.idata

import com.google.gson.Gson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT





//const val BASE_URL = "https://tlyr3e3uvd.execute-api.us-east-2.amazonaws.com/"
const val BASE_URL = "https://aaeh7rnpl1.execute-api.us-east-1.amazonaws.com/"
@OptIn(ExperimentalSerializationApi::class)
val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

object AwsApi{
    val retrofitService: AwsApiService by lazy{
        retrofit.create(AwsApiService::class.java)
    }
}
interface AwsApiService{
    @GET("items")
    suspend fun getAllDevices(): List<DeviceData>

    @PUT("items")
    suspend fun onOffSwitch(/*@Path("{items}") items:,*/ @Body deviceData: DeviceData): Call<DeviceData>
}

