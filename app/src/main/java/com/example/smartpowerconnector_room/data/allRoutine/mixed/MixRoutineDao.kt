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

package com.example.smartpowerconnector_room.data.allRoutine.mixed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smartpowerconnector_room.data.allRoutine.mixed.MixRoutine
import kotlinx.coroutines.flow.Flow

/////////////////////////delete

/**
 * Database access object to access the Inventory database
 */
@Dao
interface MixRoutineDao {

//    @Query("SELECT * from timerRoutine ORDER BY name ASC")
//    fun getAllRoutines(): Flow<List<TimerRoutine>>


    @Query("SELECT * from mixRoutine ORDER BY name ASC")
    fun getAllMixRoutines(): Flow<List<MixRoutine>>


    @Query("SELECT * from mixRoutine WHERE id = :id")
    fun getMixRoutine(id: Int): Flow<MixRoutine>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(routine: MixRoutine)


    @Update
    suspend fun update(routine: MixRoutine)


    @Delete
    suspend fun delete(routine: MixRoutine)



}
