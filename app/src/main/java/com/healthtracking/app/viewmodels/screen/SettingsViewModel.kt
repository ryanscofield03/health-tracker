package com.healthtracking.app.viewmodels.screen

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SettingsViewModel(context: Context) : ViewModel() {
    companion object {
        const val LANGUAGE_KEY = "language"
        const val LANGUAGE_DEFAULT = "english"

        const val MEASUREMENTS_KEY = "measurements"
        const val MEASUREMENTS_METRIC = "metric"
        const val MEASUREMENTS_IMPERIAL = "imperial"

        const val NOTIFICATIONS_KEY = "notifications"
        const val NOTIFICATIONS_DEFAULT = true
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", MODE_PRIVATE)

    private val _languageSetting = MutableStateFlow(sharedPreferences.getString(
        LANGUAGE_KEY, LANGUAGE_DEFAULT) ?: LANGUAGE_DEFAULT)
    val languageSetting: StateFlow<String> get() = _languageSetting

    private val _measurementsSetting = MutableStateFlow(sharedPreferences.getString(
        MEASUREMENTS_KEY, MEASUREMENTS_METRIC) ?: MEASUREMENTS_METRIC)
    val measurementsSetting: StateFlow<String> get() = _measurementsSetting

    private val _notificationsSetting = MutableStateFlow(sharedPreferences.getBoolean(
        NOTIFICATIONS_KEY, NOTIFICATIONS_DEFAULT))
    val notificationsSetting: StateFlow<Boolean> get() = _notificationsSetting

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            when (key) {
                LANGUAGE_KEY -> _languageSetting.value = sharedPreferences.getString(
                    LANGUAGE_KEY, LANGUAGE_DEFAULT) ?: LANGUAGE_DEFAULT
                MEASUREMENTS_KEY -> _measurementsSetting.value = sharedPreferences.getString(
                    MEASUREMENTS_KEY, MEASUREMENTS_METRIC) ?: MEASUREMENTS_METRIC
                NOTIFICATIONS_KEY -> _notificationsSetting.value = sharedPreferences.getBoolean(
                    NOTIFICATIONS_KEY, NOTIFICATIONS_DEFAULT)
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener { _, _ -> }
    }
}