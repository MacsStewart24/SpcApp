package com.example.smartpowerconnector_room.alarm

import com.example.smartpowerconnector_room.data.Device
import com.example.smartpowerconnector_room.internet.idata.NetworkDeviceRepository
import com.example.smartpowerconnector_room.ui.routinescreens.routine.TimerRoutineDetails
import java.time.LocalDateTime

data class AlarmData (
    val time: LocalDateTime,
    val routineId: String,
    val timerRoutine: TimerRoutineDetails
    )