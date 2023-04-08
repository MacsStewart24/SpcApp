package com.example.smartpowerconnector_room.internet.idata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class DeviceData(

    @SerialName(value = "id" )
    val deviceName: String,

    @SerialName(value = "id1" )
    val deviceId: String,

    @SerialName(value = "id2" )
    val deviceStatus: String,

    @SerialName(value = "id3" )
    val deviceDescription: String,
)

@Serializable(with = MyDeserializer::class)
data class MyClass(val myProperty: String)

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass =  MyClass::class)
object MyDeserializer : KSerializer< MyClass> {
    override fun deserialize(decoder: Decoder):  MyClass {
        val json = decoder.decodeJsonElement()
        val myProperty = when (json) {
            is JsonPrimitive -> json.content
            else -> throw JsonDecodingException("Expected string literal, but got $json")
        }
        return MyClass(myProperty)
    }
}