/*
 * Copyright (C) 2022 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.smartpowerconnector_room.data.allRoutine.multi

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smartpowerconnector_room.data.allRoutine.multi.MultiRoutine
import kotlinx.coroutines.flow.Flow

/////////////////////////delete

/**
 * Database access object to access the Inventory database
 */
@Dao
interface MultiRoutineDao {

//    @Query("SELECT * from timerRoutine ORDER BY name ASC")
//    fun getAllRoutines(): Flow<List<TimerRoutine>>


    @Query("SELECT * from multiRoutine ORDER BY name ASC")
    fun getAllMultiRoutines(): Flow<List<MultiRoutine>>


    @Query("SELECT * from multiRoutine WHERE id = :id")
    fun getMultiRoutine(id: Int): Flow<MultiRoutine>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(routine: MultiRoutine)


    @Update
    suspend fun update(routine: MultiRoutine)


    @Delete
    suspend fun delete(routine: MultiRoutine)



}

