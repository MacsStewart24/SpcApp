package com.example.smartpowerconnector_room.internet.idata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.*

@Serializable
data class DeviceData(
    @SerialName(value = "id" )  val deviceName: String,
    @SerialName(value = "id1" ) val deviceId: String,
    @SerialName(value = "id2" ) val deviceStatus: String,
    @SerialName(value = "id3" ) val deviceDescription: String,
    //@SerialName(value = "id4" ) val deviceTime: Int,
    )


@Serializable
data class Usage(
    @SerialName(value = "boardID" )  val boardID: String,
    @SerialName(value = "timestamp" ) val timestamp: String,
    @SerialName(value = "current" ) val current: Int,
    @SerialName(value = "status" ) val status: String,
    @SerialName(value = "voltage" ) val voltage: Int,
)