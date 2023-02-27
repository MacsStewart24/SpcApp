package com.example.smartpowerconnector_room.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Provides the app with instances of the Dao's I defined. In turn, the app can use the DAO s to
// retrieve data from the database as instances of the associated data entity objects. The app
// can also use the data entities to update rows form the corresponding tables or to create new
// rows of insertion.


// This is an extension of the RoomDatabase class.
@Database(entities = [Device::class], version = 1, exportSchema = false) // Marks this as a RoomDatabase
abstract class DeviceDatabase: RoomDatabase(){

    abstract fun deviceDao(): DeviceDao // Notifies the database of the DAO

    companion object{
        @Volatile
        private var Instance: DeviceDatabase? = null

        fun getDatabase( context: Context): DeviceDatabase{
            return Instance ?: synchronized(this){ //Prevents multiple instances of the database
                Room.databaseBuilder(context, DeviceDatabase::class.java, "device_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}