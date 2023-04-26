package com.example.smartpowerconnector_room.alarm.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.smartpowerconnector_room.alarm.AlarmData
import com.example.smartpowerconnector_room.alarm.AlarmInterface
import com.example.smartpowerconnector_room.internet.idata.AwsRepository
import java.time.ZoneId

class AlarmScheduler(private val context: Context): AlarmInterface {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(alarmData: AlarmData) {
        val intent = Intent(context,AlarmReceiver::class.java).apply {
            putExtra("RName", alarmData.routineId)
            putExtra("deviceStatus", alarmData.timerRoutine.status)
            putExtra("deviceName", alarmData.timerRoutine.deviceName)
            putExtra("description", alarmData.timerRoutine.deviceDescription)
            putExtra("deviceId", alarmData.timerRoutine.deviceId)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmData.time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            PendingIntent.getBroadcast(
                context,
                alarmData.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(alarmData: AlarmData) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmData.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}