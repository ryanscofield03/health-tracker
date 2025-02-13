package com.healthtracking.app.viewmodels.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(context: Context) : ViewModel() {
    companion object {
        const val LANGUAGE_KEY = "language"
        const val LANGUAGE_DEFAULT = "English (US)"

        const val MEASUREMENTS_KEY = "measurements"
        const val MEASUREMENTS_METRIC = "metric"
        const val MEASUREMENTS_DEFAULT = MEASUREMENTS_METRIC
        const val MEASUREMENTS_IMPERIAL = "imperial"

        const val NOTIFICATIONS_KEY = "notifications"
        const val NOTIFICATIONS_DEFAULT = true
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", MODE_PRIVATE)

    private val _languageSetting = MutableStateFlow(sharedPreferences.getString(
        LANGUAGE_KEY, LANGUAGE_DEFAULT
    ) ?: LANGUAGE_DEFAULT
    )
    val languageSetting: StateFlow<String> = _languageSetting

    private val _measurementsSetting = MutableStateFlow(sharedPreferences.getString(
        MEASUREMENTS_KEY, MEASUREMENTS_DEFAULT
    ) ?: MEASUREMENTS_DEFAULT
    )
    val measurementsSetting: StateFlow<String> = _measurementsSetting

    private val _notificationsSetting = MutableStateFlow(sharedPreferences.getBoolean(
        NOTIFICATIONS_KEY, NOTIFICATIONS_DEFAULT
    ))
    val notificationsSetting: StateFlow<Boolean> = _notificationsSetting

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            LANGUAGE_KEY -> _languageSetting.value = sharedPreferences.getString(
                LANGUAGE_KEY, LANGUAGE_DEFAULT) ?: LANGUAGE_DEFAULT
            MEASUREMENTS_KEY -> _measurementsSetting.value = sharedPreferences.getString(
                MEASUREMENTS_KEY, MEASUREMENTS_DEFAULT) ?: MEASUREMENTS_DEFAULT
            NOTIFICATIONS_KEY -> _notificationsSetting.value = sharedPreferences.getBoolean(
                NOTIFICATIONS_KEY, NOTIFICATIONS_DEFAULT)
        }
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    fun saveLanguageSetting(newLanguage: String) {
        sharedPreferences
            .edit()
            .putString(LANGUAGE_KEY, newLanguage)
            .apply()
    }

    fun saveMeasurementsSetting(newMeasurements: String) {
        sharedPreferences
            .edit()
            .putString(MEASUREMENTS_KEY, newMeasurements)
            .apply()
    }

    fun saveNotificationsSetting() {
        sharedPreferences
            .edit()
            .putBoolean(NOTIFICATIONS_KEY, !notificationsSetting.value)
            .apply()
    }
}