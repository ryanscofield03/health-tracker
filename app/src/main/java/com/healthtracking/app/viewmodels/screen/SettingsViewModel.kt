package com.healthtracking.app.viewmodels.screen

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel


class SettingsViewModel(context: Context): ViewModel() {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", MODE_PRIVATE)

    val language get() = sharedPreferences.getString("language", "en") ?: "en"
    val measurements get() = sharedPreferences.getString("measurements", "metric") ?: "metric"
    val notifications get() = sharedPreferences.getString("notifications", "on") ?: "on"

    fun saveLanguage(newLanguage: String) {
        sharedPreferences.edit()
            .putString("language", newLanguage)
            .apply()
    }

    fun saveMeasurements(newMeasurements: String) {
        sharedPreferences.edit()
            .putString("measurements", newMeasurements)
            .apply()
    }

    fun saveNotifications(newNotification: String) {
        sharedPreferences.edit()
            .putString("notifications", newNotification)
            .apply()
    }
}