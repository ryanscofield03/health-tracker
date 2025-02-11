package com.healthtracking.app.viewmodels.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.healthtracking.app.entities.MealWithFoodList
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.viewmodels.database.SleepViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class SleepScreenViewModel(
    private val sleepViewModel: SleepViewModel
) : ViewModel() {
    companion object {
        private const val DEFAULT_RATING_VALUE = 3
        @OptIn(ExperimentalMaterial3Api::class)
        private val DEFAULT_START_TIME_PICKER_STATE =
            TimePickerState(initialMinute = 0, initialHour = 22, is24Hour = false)
        @OptIn(ExperimentalMaterial3Api::class)
        private val DEFAULT_END_TIME_PICKER_STATE =
            TimePickerState(initialMinute = 0, initialHour = 6, is24Hour = false)

        private val DEFAULT_SLEEP_ID_VALUE = null
        private val DEFAULT_ENTRY_DATE_VALUE = LocalDate.now()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    var startTimePickerState: TimePickerState = DEFAULT_START_TIME_PICKER_STATE
    @OptIn(ExperimentalMaterial3Api::class)
    var endTimePickerState: TimePickerState = DEFAULT_END_TIME_PICKER_STATE

    private val _rating = mutableIntStateOf(DEFAULT_RATING_VALUE)
    val rating: Int get() = _rating.intValue

    private val _pastSleepEntries: MutableState<Flow<List<Sleep>?>> = mutableStateOf(flow{listOf<Sleep>()})
    val pastSleepEntries: Flow<List<Sleep>?> get() = _pastSleepEntries.value

    private val _editSleepId: MutableState<Long?> = mutableStateOf(DEFAULT_SLEEP_ID_VALUE)
    val editSleepId: Long? get() = _editSleepId.value

    private val _dateOfEntry: MutableState<LocalDate> = mutableStateOf(DEFAULT_ENTRY_DATE_VALUE)
    val dateOfEntry get() = _dateOfEntry.value

    init {
        viewModelScope.launch {
            _pastSleepEntries.value = sleepViewModel.getSleepEntries()
        }
    }

    /**
     * Saves the sleep entry to persistence
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun saveSleepEntry() {
        viewModelScope.launch {
            val newEntryStartTime: LocalTime = LocalTime.of(startTimePickerState.hour, startTimePickerState.minute)
            val newEntryEndTime: LocalTime = LocalTime.of(endTimePickerState.hour, endTimePickerState.minute)

            if (_editSleepId.value != null) {
                sleepViewModel.updateSleepEntry(
                    id = _editSleepId.value!!,
                    startTime = newEntryStartTime,
                    endTime = newEntryEndTime,
                    rating = rating
                )
            } else {
                sleepViewModel.addSleepEntry(
                    date = LocalDate.now(),
                    startTime = newEntryStartTime,
                    endTime = newEntryEndTime,
                    rating = rating
                )
            }

            clearSleepEntry()
        }
    }

    /**
     * Clears the sleep entry (e.g. cancels or saves)
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun clearSleepEntry() {
        _rating.intValue = DEFAULT_RATING_VALUE
        startTimePickerState = DEFAULT_START_TIME_PICKER_STATE
        endTimePickerState = DEFAULT_END_TIME_PICKER_STATE
        _editSleepId.value = DEFAULT_SLEEP_ID_VALUE
        _dateOfEntry.value = DEFAULT_ENTRY_DATE_VALUE
    }

    /**
     * Sets the rating to a new Int value
     */
    fun updateRating(newRating: Int) {
        _rating.intValue = newRating
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun editSleepEntry(sleepId: Long) {
        viewModelScope.launch {
            val sleepEntity = sleepViewModel.getSleepEntry(sleepId)

            if (sleepEntity != null) {
                _editSleepId.value = sleepId
                _rating.intValue = sleepEntity.rating
                startTimePickerState = TimePickerState(
                    initialHour = sleepEntity.startTime.hour,
                    initialMinute = sleepEntity.startTime.minute,
                    is24Hour = false
                )
                endTimePickerState = TimePickerState(
                    initialHour = sleepEntity.endTime.hour,
                    initialMinute = sleepEntity.endTime.minute,
                    is24Hour = false
                )
                _dateOfEntry.value = sleepEntity.date
            }
        }
    }
}