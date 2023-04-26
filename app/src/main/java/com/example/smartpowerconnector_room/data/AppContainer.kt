package com.example.smartpowerconnector_room.data

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.example.smartpowerconnector_room.DeviceApplication
import com.example.smartpowerconnector_room.alarm.AlarmInterface
import com.example.smartpowerconnector_room.alarm.data.AlarmScheduler
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutineDatabase
import com.example.smartpowerconnector_room.data.allRoutine.clock.ClockRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutineDatabase
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutineDatabase
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.offline.OfflineClockRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.offline.OfflineMixRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.offline.OfflineMultiRoutineRepository
import com.example.smartpowerconnector_room.data.allRoutine.offline.OfflineRoutinesRepository
import com.example.smartpowerconnector_room.data.allRoutine.routine.RoutineDatabase
import com.example.smartpowerconnector_room.data.allRoutine.routine.RoutinesRepository
import com.example.smartpowerconnector_room.internet.idata.*
import com.example.smartpowerconnector_room.work.RoutineWorkManager
import com.example.smartpowerconnector_room.work.WorkRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer{
    val deviceRepository: DeviceRepository
    val awsRepository: AwsRepository
    val routinesRepository: RoutinesRepository
    val clockRoutineRepository: ClockRoutineRepository
    val multiRoutineRepository: MultiRoutineRepository
    val mixRoutineRepository: MixRoutineRepository
    val alarmInterface: AlarmInterface
    val workRepository: WorkRepository
    val useRepository: UseRepository
}

//@AndroidEntryPoint
class AppDataContainer(private val context: Context): AppContainer{
    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(DeviceDatabase.getDatabase(context).deviceDao())
    }
    override val routinesRepository: RoutinesRepository by lazy {
        OfflineRoutinesRepository(RoutineDatabase.getDatabase(context).routineDao())
    }
    override val clockRoutineRepository: ClockRoutineRepository by lazy {
        OfflineClockRoutineRepository(ClockRoutineDatabase.getDatabase(context).ClockRoutineDao())
    }
    override val multiRoutineRepository: MultiRoutineRepository by lazy {
        OfflineMultiRoutineRepository(MultiRoutineDatabase.getDatabase(context).MultiRoutineDao())
    }
    override val mixRoutineRepository: MixRoutineRepository by lazy {
        OfflineMixRoutineRepository(MixRoutineDatabase.getDatabase(context).MixRoutineDao())
    }

    override val alarmInterface = AlarmScheduler(context)


    // Internet Portion of the app container
    //val BASE_URL = "https://tlyr3e3uvd.execute-api.us-east-2.amazonaws.com/"
    val BASE_URL = "https://aaeh7rnpl1.execute-api.us-east-1.amazonaws.com/"

    //Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()


     //Retrofit service object for creating api calls
    private val retrofitService: AwsApiService by lazy{
        retrofit.create(AwsApiService::class.java)
    }

    override val awsRepository: AwsRepository by lazy {
        NetworkDeviceRepository(retrofitService)
    }
    val BASE_URL2 = "https://jwkyopdcml.execute-api.us-east-1.amazonaws.com/"

    //Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
    private val retrofit2: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL2)
        .build()

    private val retrofitService2: AwsApi by lazy{
        retrofit2.create(AwsApi::class.java)
    }

    override val  useRepository: UseRepository by lazy {
        NetworkUsagesRepository(retrofitService2)
    }

    override val workRepository: WorkRepository = RoutineWorkManager(context)
}