package com.healthtracking.app.composables.screens.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthtracking.app.R
import com.healthtracking.app.composables.BackgroundBorderBox
import com.healthtracking.app.composables.HeaderAndListBox
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.composables.TimeInputDisplay
import com.healthtracking.app.services.calculateTimeSlept
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.viewmodels.screen.SleepScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepMain(
    modifier: Modifier,
    viewModel: SleepScreenViewModel
) {
    val sleepEntries by viewModel.pastSleepEntries.collectAsState(emptyList())

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // past sleep entries
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderAndListBox(
                header = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.past_sleep_entries),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left
                    )
                },
                listContent = {
                    SleepEntriesList(
                        pastSleepEntries = sleepEntries!!,
                        editSleepEntry = { viewModel.editSleepEntry(it) }
                    )
                },
                isContentEmpty = sleepEntries == null || sleepEntries!!.isEmpty(),
                contentPlaceholderText = stringResource(id = R.string.no_existing_sleep_entries)
            )
        }


        // state for adding new entry
        val canAddNewEntry by remember {
            derivedStateOf {
                val hasEntryToday = sleepEntries?.any { it.date == LocalDate.now() } ?: false

                // can add new entry if there is no entry today or editSleepId is not null
                viewModel.editSleepId != null || !hasEntryToday
            }
        }
        // add/edit sleep entry box
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            BackgroundBorderBox {
                AddSleepEntry(
                    dateOfEntry = viewModel.dateOfEntry,
                    startTimePickerState = viewModel.startTimePickerState,
                    endTimePickerState = viewModel.endTimePickerState,
                    rating = viewModel.rating,
                    onRatingChange = { viewModel.updateRating(it) },
                    saveSleepEntry = { viewModel.saveSleepEntry() },
                    canAddNewEntry = canAddNewEntry
                )
            }
        }
    }
}

@Composable
fun SleepEntriesList(
    pastSleepEntries: List<Sleep>,
    editSleepEntry: (Long) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = LazyListState(firstVisibleItemIndex = pastSleepEntries.size-1)
    ) {
        itemsIndexed(pastSleepEntries) { _, pastSleepEntry ->
            PastSleepEntryCard(
                pastSleepEntry = pastSleepEntry,
                openEditSleepEntry = { editSleepEntry(pastSleepEntry.id) }
            )
        }
    }
}

@Composable
fun PastSleepEntryCard(
    pastSleepEntry: Sleep,
    openEditSleepEntry: () -> Unit
) {
    val hoursSlept: String = calculateTimeSlept(
        startTime = pastSleepEntry.startTime,
        endTime = pastSleepEntry.endTime
    )

    // maybe add suffixes here
    val formattedDate = pastSleepEntry.date.format(
        DateTimeFormatter.ofPattern("dd MMMM")
    )

    val ratingIconModifier = Modifier.size(40.dp)
    val ratingIcon: @Composable () -> Unit = when (pastSleepEntry.rating) {
        1 -> {
            { Icon(
                modifier = ratingIconModifier,
                painter = painterResource(id = R.drawable.large_frown),
                contentDescription = stringResource(id = R.string.terrible)
            ) }
        }
        2 -> {
            { Icon(
                modifier = ratingIconModifier,
                painter = painterResource(id = R.drawable.small_frown),
                contentDescription = stringResource(id = R.string.bad)
            ) }
        }
        3 -> {
            { Icon(
                modifier = ratingIconModifier,
                painter = painterResource(id = R.drawable.straight_face),
                contentDescription = stringResource(id = R.string.ok)
            ) }
        }
        4 -> {
            { Icon(
                modifier = ratingIconModifier,
                painter = painterResource(id = R.drawable.small_smile),
                contentDescription = stringResource(id = R.string.good)
            ) }
        }
        5 -> {
            { Icon(
                modifier = ratingIconModifier,
                painter = painterResource(id = R.drawable.large_smile),
                contentDescription = stringResource(id = R.string.great)
            ) }
        }
        else -> {
            { Icon(
                modifier = ratingIconModifier,
                painter = painterResource(id = R.drawable.large_frown),
                contentDescription = stringResource(id = R.string.terrible)
            ) }
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = {
                openEditSleepEntry()
            }),
        shape = CustomCutCornerShape,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Slept $hoursSlept hours",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }

            ratingIcon()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSleepEntry(
    dateOfEntry: LocalDate,
    startTimePickerState: TimePickerState,
    endTimePickerState: TimePickerState,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    saveSleepEntry: () -> Unit,
    canAddNewEntry: Boolean
) {
    val timeInputColours = TimePickerDefaults.colors(
        periodSelectorBorderColor = Color.Gray,
        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.2f),
        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onTertiary,
        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onTertiary,
        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.6f),
    )

    val startTimePickerOpened = rememberSaveable { mutableStateOf(false) }
    val endTimePickerOpened = rememberSaveable { mutableStateOf(false) }

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = dateOfEntry.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
        style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Right
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (startTimePickerOpened.value){
            TimeInputDialog(
                title = stringResource(id = R.string.start_time_title),
                state = startTimePickerState,
                onDismissRequest = { startTimePickerOpened.value = false },
                colors = timeInputColours
            )
        }
        Box(modifier = Modifier
            .clickable(
                enabled = canAddNewEntry,
                onClick = { startTimePickerOpened.value = true }
            )
        ) {
            TimeInputDisplay(
                title = stringResource(id = R.string.start_time_title),
                state = startTimePickerState,
                colors = timeInputColours,
                enabled = canAddNewEntry
            )
        }

        if (endTimePickerOpened.value){
            TimeInputDialog(
                title = stringResource(id = R.string.end_time_title),
                state = endTimePickerState,
                onDismissRequest = { endTimePickerOpened.value = false },
                colors = timeInputColours
            )
        }
        Box(modifier = Modifier
            .clickable(
                enabled = canAddNewEntry,
                onClick = { startTimePickerOpened.value = true }
            )
        ) {
            TimeInputDisplay(
                title = stringResource(id = R.string.end_time_title),
                state = endTimePickerState,
                colors = timeInputColours,
                enabled = canAddNewEntry
            )
        }

        RatingPicker(
            rating = rating,
            onRatingChange = onRatingChange,
            enabled = canAddNewEntry
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            onClick = saveSleepEntry,
            enabled = canAddNewEntry
        ) {
            if (canAddNewEntry) {
                Text(text = stringResource(id = R.string.save_sleep_entry))
            } else {
                Text(text = stringResource(id = R.string.cannot_add_sleep_entry))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeInputDialog(
    title: String,
    state: TimePickerState,
    onDismissRequest: () -> Unit,
    colors: TimePickerColors
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                TimePicker(state = state, colors = colors)

                TextButton(
                    modifier = Modifier.align(alignment = Alignment.End),
                    onClick = onDismissRequest)
                {
                    Text(stringResource(id = R.string.close))
                }
            }
        }
    }
}

@Composable
private fun RatingPicker(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    enabled: Boolean = true
) {
    Row(modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceBetween) {
        SleepRatingButton(
            iconId = R.drawable.large_frown,
            descriptionId = R.string.terrible,
            enabled = enabled,
            onClick = { onRatingChange(1) },
            isSelected = rating == 1
        )
        SleepRatingButton(
            iconId = R.drawable.small_frown,
            descriptionId = R.string.bad,
            enabled = enabled,
            onClick = { onRatingChange(2) },
            isSelected = rating == 2
        )
        SleepRatingButton(
            iconId = R.drawable.straight_face,
            descriptionId = R.string.ok,
            enabled = enabled,
            onClick = { onRatingChange(3) },
            isSelected = rating == 3
        )
        SleepRatingButton(
            iconId = R.drawable.small_smile,
            descriptionId = R.string.good,
            enabled = enabled,
            onClick = { onRatingChange(4) },
            isSelected = rating == 4
        )
        SleepRatingButton(
            iconId = R.drawable.large_smile,
            descriptionId = R.string.great,
            enabled = enabled,
            onClick = { onRatingChange(5) },
            isSelected = rating == 5
        )
    }
}

@Composable
private fun SleepRatingButton(
    iconId: Int,
    descriptionId: Int,
    enabled: Boolean = true,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(
                color =
                if (enabled && isSelected) MaterialTheme.colorScheme.tertiary
                else if (isSelected) MaterialTheme.colorScheme.surfaceVariant
                else Color.Transparent
            ),
        onClick = {
            if (enabled) {
                onClick()
            }
        }
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = descriptionId)
        )
    }
}