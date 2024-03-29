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

package com.example.smartpowerconnector_room.data.allRoutine.routine

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "timerRoutine")
/*
This routine allows the user to turn a device on or off after a set period of time
 */
data class TimerRoutine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deviceId: String,
    val name: String,
    val endTime: String,
    val startTime: String,
    val status: String,
    val duration: String
)



