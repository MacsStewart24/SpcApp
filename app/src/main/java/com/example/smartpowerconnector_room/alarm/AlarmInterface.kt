package com.example.smartpowerconnector_room.alarm

interface AlarmInterface {
    fun schedule(alarmData: AlarmData)
    fun cancel(alarmData: AlarmData)
}