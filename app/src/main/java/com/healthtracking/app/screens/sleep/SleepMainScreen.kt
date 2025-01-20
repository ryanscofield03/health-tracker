package com.healthtracking.app.screens.sleep

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.viewmodels.screen.SleepScreenViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepMain (
    modifier: Modifier,
    viewModel: SleepScreenViewModel
) {
    val sleepEntries by viewModel.pastSleepEntries.observeAsState(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.sleep_screen),
            style = MaterialTheme.typography.displaySmall
        )

        if (sleepEntries != null && sleepEntries!!.isNotEmpty()) {
            PastSleepEntries(pastSleepEntries = sleepEntries!!)
        } else {
            Text(text = "Make some sleep entries")
        }

        AddSleepEntry(
            startTimePickerState = viewModel.startTimePickerState,
            endTimePickerState = viewModel.EndTimePickerState
        )
    }
}

@Composable
fun PastSleepEntries(
    pastSleepEntries: List<Sleep>
) {
    LazyColumn() {
        itemsIndexed(pastSleepEntries) { _, pastSleepEntry ->
            PastSleepEntryCard(pastSleepEntry)
        }
    }
}

@Composable
fun PastSleepEntryCard(
    pastSleepEntry: Sleep
) {
    Card {
        Text(text = pastSleepEntry.date.format(DateTimeFormatter.ISO_DATE))
        Text(text = pastSleepEntry.rating.toString())
        Text(text = pastSleepEntry.startTime.format(DateTimeFormatter.ISO_DATE))
        Text(text = pastSleepEntry.endTime.format(DateTimeFormatter.ISO_DATE))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepEntry(
    startTimePickerState: TimePickerState,
    endTimePickerState: TimePickerState,
) {
    TimeInput(state = startTimePickerState)
    TimeInput(state = endTimePickerState)
}
