package com.example.smartpowerconnector_room.data

import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    //Retrieves all data from the given data source
    fun getAllDevicesStream(): Flow<List<Device>>

    //Retrieve a single device from the given source
    fun getDeviceStream(id: Int): Flow<Device?>

    suspend fun getDevice(id: String): Flow<Device?>

    suspend fun insertDevice(device: Device)            // Add Item

    suspend fun deleteDevice(device: Device)            // Delete Item

    suspend fun updateDevice(device: Device)            // Update Item
}

