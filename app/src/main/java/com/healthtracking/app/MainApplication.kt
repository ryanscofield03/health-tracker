package com.healthtracking.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(com.healthtracking.app.dataAccessModule))
        }
    }
}

/**
 * TODO:
 *
 * - Settings (1) : Easiest and has a reasonably big impact (>20 hrs)
 * - Sleep (2) : 2nd Easiest and is important (>20-30 hrs)
 * - Eat (3) : May be a lot of work, big impact too but depends on how extensive my implementation is (30-40 hrs)
 * - Stats (4) : Important but has to wait on other features first, should be quite quick to implement (~20 hours)
 * - Workout (5) : Fix small issues, allow for more customisation (measurement types, kg vs. lb, timer, etc)
 */