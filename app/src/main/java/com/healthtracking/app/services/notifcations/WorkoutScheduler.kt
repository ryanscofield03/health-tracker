package com.healthtracking.app.services.notifcations

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

const val UNIQUE_WORK_NAME = "remind workout"

fun scheduleWorkoutReminder(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresBatteryNotLow(true)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<WorkoutReminderWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        uniqueWorkName = UNIQUE_WORK_NAME,
        existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
        request = workRequest
    )

    // Debugging code to observe the work status
    val workManager = WorkManager.getInstance(context)
    workManager.getWorkInfosForUniqueWorkLiveData(UNIQUE_WORK_NAME).observeForever { workInfos ->
        workInfos?.forEach { workInfo ->
            Log.d("WorkManagerDebug", "Work Status: ${workInfo.state}")
        }
    }
}

private fun calculateInitialDelay(): Long {
    val now = LocalDateTime.now()
    val targetTime = now.withHour(6).withMinute(0).withSecond(0)

    var delay = Duration.between(now, targetTime).toMillis()
    if (delay < 0) {
        delay += Duration.ofDays(1).toMillis()
    }

    return delay
}