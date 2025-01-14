package nz.ac.canterbury.seng303.healthtracking

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(dataAccessModule))
        }
    }
}

/**
 * TODO:
 * - Allow for running workout (8 hrs ?)
 *  * Validation checks e.g. don't allow the user to finish a workout til they have at least 1 set for each exercise
 *   - or potentially have a pop up that says "You don't have a set for each exercise, are you sure you want to finish workout"
 *
 * - Sleep, Eat, Stats, Settings
 *
 * - Settings (1) : Easiest and has a reasonably big impact (>20 hrs)
 * - Sleep (2) : 2nd Easiest and is important (>20-30 hrs)
 * - Eat (3) : May be a lot of work, big impact too but depends on how extensive my implementation is (30-40 hrs)
 */