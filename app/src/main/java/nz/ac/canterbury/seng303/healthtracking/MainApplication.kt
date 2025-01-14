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
 * - Sleep, Eat, Stats, Settings
 *
 * - Settings (1) : Easiest and has a reasonably big impact (>20 hrs)
 * - Sleep (2) : 2nd Easiest and is important (>20-30 hrs)
 * - Eat (3) : May be a lot of work, big impact too but depends on how extensive my implementation is (30-40 hrs)
 */