package com.example.smartpowerconnector_room.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "devices")
data class Device (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deviceName: String,
    val deviceId: String,// Added _ to name
    val deviceStatus: String,
    val deviceDescription: String,
    val deviceTime: Int
    //val deviceRoutine: Int,
    //val RoutineStatus: Boolean, ON->OFF Or OFF->ON
)




// Do you want to set a routine? Yes or No
// Do you want a timer or a global time?
// Selected timer: timer -> DeviceRoutine, Global time-> null

//APP->SERVER->Device
//!APP->Device

//  Entity structure
//  --------------------------------------------------------
//  |ID|deviceName |deviceID|deviceStatus|deviceDescription|
//  |1 |FirstDevice|12:ab   |OFF         |living room      |
//  --------------------------------------------------------



