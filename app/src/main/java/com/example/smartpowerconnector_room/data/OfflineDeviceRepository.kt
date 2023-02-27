package com.example.smartpowerconnector_room.data

import kotlinx.coroutines.flow.Flow

class OfflineDeviceRepository(private val deviceDao: DeviceDao): DeviceRepository {

    override fun getAllDevicesStream(): Flow<List<Device>> = deviceDao.getAllDevices()
    override fun getDeviceStream(id: Int): Flow<Device?> = deviceDao.getItem(id)
    override suspend fun insertDevice(device: Device) =deviceDao.insert(device)
    override suspend fun updateDevice(device: Device)  = deviceDao.update(device)
    override suspend fun deleteDevice(device: Device) = deviceDao.delete(device)
}