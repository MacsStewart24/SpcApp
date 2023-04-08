package com.example.smartpowerconnector_room.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//Dao's (Data Accessible Object) provide methods for retrieving, updating, inserting, and deleting data stored in rows
@Dao
interface DeviceDao {
    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database. Room ignores the conflict.
    // Because we are only expecting items to be added from the add Item screen for now
    // FIXME: You will need to allow for the app to download information from the server
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(device: Device)              // Note: Suspend allows for the insert function to run on a background thread.

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertAllDevices(device: List<Device>)

    @Update
    suspend fun update(device: Device)

    @Delete
    suspend fun delete (device: Device)

    @Query("SELECT * from devices ORDER BY deviceName ASC")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * from devices WHERE id = :id ")         //Fixme: Add search options for the names of the devices
    fun getItem(id: Int): Flow<Device>
}