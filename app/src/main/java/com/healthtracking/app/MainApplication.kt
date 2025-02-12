package com.healthtracking.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.healthtracking.app.services.notifcations.scheduleWorkoutReminder
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        scheduleWorkoutReminder(this)

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(dataAccessModule))
        }
    }
}