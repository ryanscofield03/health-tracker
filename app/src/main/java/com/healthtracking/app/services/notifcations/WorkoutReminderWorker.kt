package com.healthtracking.app.services.notifcations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.healthtracking.app.R
import com.healthtracking.app.daos.WorkoutDao
import com.healthtracking.app.entities.Workout
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.random.Random

class WorkoutReminderWorker(
    val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        const val CHANNEL_ID = "workout_reminder_channel_id"
        const val CHANNEL_NAME = "workout reminder channel"
    }

    private val workoutDao : WorkoutDao by inject()

    override suspend fun doWork(): Result {
        val workoutFlow: Flow<List<Workout>?> = workoutDao.getAllWorkouts()

        workoutFlow.collect { workouts ->
            if (!workouts.isNullOrEmpty()) {
                // find workout that is scheduled for today
                val workout: Workout? = workouts.find { workout ->
                    workout.schedule.any { day: Pair<DayOfWeek, LocalDate> ->
                        LocalDate.now().dayOfWeek == day.first && day.second != LocalDate.now()
                    }
                }

                if (workout != null) {
                    sendNotification(workout)

                    // update workout to mark notification as sent
                    workoutDao.upsertWorkout(workout.copy(schedule = workout.schedule.map {
                        if (it.first == LocalDate.now().dayOfWeek) {
                            it.copy(second = LocalDate.now())
                        } else {
                            it
                        }
                    }))
                }
            }
        }

        return Result.success()
    }


    private fun sendNotification(workout: Workout) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val workoutNameFormatted = workout.name.trim().lowercase()
        val workoutDescriptionFormatted = workout.description.trim().lowercase()
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.heart)
            .setContentTitle(context.getString(R.string.workout_reminder_title, workoutNameFormatted))
            .setContentText(context.getString(R.string.workout_reminder_subtitle, workoutDescriptionFormatted))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}