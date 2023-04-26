package com.example.smartpowerconnector_room.work

interface WorkRepository {
    fun scheduleRoutine(workerData: WorkerData)
}

data class WorkerData(
    val routineId: String,
    val duration: String,
    val deviceName: String,
    val deviceId: String,
    val deviceStatus: String,
    val deviceDescription: String,
)
