package com.example.smartpowerconnector_room.data

import android.content.Context
import com.example.smartpowerconnector_room.internet.idata.AwsApiService
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import com.example.smartpowerconnector_room.internet.idata.NetworkDeviceRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//import com.example.smartpowerconnector_room.internet.idata.AwsApiService
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.json.Json
//import okhttp3.MediaType.Companion.toMediaType
//import retrofit2.Retrofit


interface AppContainer{
    val deviceRepository: DeviceRepository
    val awsRepository: AwsRepository
}

class AppDataContainer(private val context: Context): AppContainer{

    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(DeviceDatabase.getDatabase(context).deviceDao())
    }

    // Internet Portion of the app container
    //val BASE_URL = "https://tlyr3e3uvd.execute-api.us-east-2.amazonaws.com/"
    val BASE_URL = "https://aaeh7rnpl1.execute-api.us-east-1.amazonaws.com/"

    //Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /*private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(com.example.smartpowerconnector_room.internet.idata.BASE_URL)
        .build()
*/
     //Retrofit service object for creating api calls
    private val retrofitService: AwsApiService by lazy{
        retrofit.create(AwsApiService::class.java)
    }

    override val awsRepository: AwsRepository by lazy {
        NetworkDeviceRepository(retrofitService)
    }
}