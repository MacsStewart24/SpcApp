package com.example.smartpowerconnector_room.work

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import com.example.inventory.R
import com.example.smartpowerconnector_room.internet.idata.AwsApiService
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import com.example.smartpowerconnector_room.internet.idata.DeviceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoutineWorkManager(context: Context): WorkRepository {
    private val workManager = WorkManager.getInstance(context)
    val unit : TimeUnit = TimeUnit.SECONDS

    override fun scheduleRoutine(workerData: WorkerData) {
        val data = Data.Builder()
        data.putString(RoutineWorker.deviceName, workerData.deviceName )
            .putString(RoutineWorker.deviceId, workerData.deviceId )
            .putString(RoutineWorker.deviceStatus, workerData.deviceStatus )
            .putString(RoutineWorker.deviceDescription, workerData.deviceDescription )

        val workRequestBuilder = OneTimeWorkRequestBuilder<RoutineWorker>()
            .setInitialDelay(workerData.duration.toLong(), unit)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            workerData.routineId,
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder,
        )
    }
}

class RoutineWorker(context: Context, workParams: WorkerParameters): CoroutineWorker(context,workParams){
    override suspend fun doWork(): Result {
            val Name = inputData.getString(deviceStatus)
            val deviceData=DeviceData(
                deviceStatus = inputData.getString(deviceStatus)!!,
                deviceId = inputData.getString(deviceId)!!,
                deviceDescription = inputData.getString(deviceDescription)!! ,
                deviceName = inputData.getString(deviceName)!!
            )
            println("Device Status: ${deviceData.deviceStatus}")
            println("Device Des: ${deviceData.deviceDescription}")
            println("Device Name: ${deviceData.deviceName}")
            println("Device Id: ${deviceData.deviceId}")

        val update = AwsApiService.instance.onOffSwitch(deviceData.deviceName,deviceData)
        update.body()?.let{
            return Result.success()
        }

        return Result.success()
    }

    companion object{
        const val deviceName = "DEVICE_NAME"
        const val deviceId = "DEVICE_ID"
        const val deviceStatus = "DEVICE_STATUS"
        const val deviceDescription = "DEVICE_DESCRIPTION"
    }
}

