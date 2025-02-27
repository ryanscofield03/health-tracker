package com.healthtracking.app.viewmodels.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.daos.SleepDao
import com.healthtracking.app.entities.Sleep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

/**
 * Used for CRUD with sleep entities
 */
class SleepViewModel(
    private val sleepDao: SleepDao
): ViewModel() {
    fun addSleepEntry(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        rating: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val sleep = Sleep(
                date = date,
                startTime = startTime,
                endTime = endTime,
                rating = rating
            )
            sleepDao.upsertSleepEntry(sleep)
        }
    }

    fun updateSleepEntry(
        id: Long,
        startTime: LocalTime,
        endTime: LocalTime,
        rating: Int
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val date = getSleepEntry(sleepId = id)?.date
            if (date != null) {
                val sleep = Sleep(
                    id = id,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    rating = rating
                )
                sleepDao.upsertSleepEntry(sleep)
            }
        }
    }

    suspend fun getSleepEntries(): Flow<List<Sleep>?> {
        return withContext(Dispatchers.IO) {
            sleepDao.getAllSleepEntries()
        }
    }

    suspend fun getSleepEntriesFlow(): Flow<List<Sleep>?> {
        return withContext(Dispatchers.IO) {
            sleepDao.getAllSleepEntriesFlow()
        }
    }

    suspend fun getSleepEntry(sleepId: Long): Sleep? {
        return withContext(Dispatchers.IO) {
            sleepDao.getSleepEntry(sleepId = sleepId)
        }
    }
}