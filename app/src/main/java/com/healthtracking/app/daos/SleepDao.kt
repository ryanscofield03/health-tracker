package com.healthtracking.app.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.healthtracking.app.entities.Sleep
import kotlinx.coroutines.flow.Flow

/**
 * DAO for sleep entities
 */
@Dao
interface SleepDao {
    @Upsert
    suspend fun upsertSleepEntry(sleepEntry: Sleep): Long

    @Query("SELECT * FROM sleep ORDER BY id ASC")
    fun getAllSleepEntries(): Flow<List<Sleep>?>

    @Query("SELECT * FROM sleep ORDER BY id ASC")
    fun getAllSleepEntriesFlow(): Flow<List<Sleep>?>

    @Query("SELECT * FROM SLEEP WHERE id = :sleepId")
    fun getSleepEntry(sleepId: Long): Sleep?
}