package com.healthtracking.app.screens.sleep

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.entities.Sleep
import com.healthtracking.app.screens.ScreenHeader
import com.healthtracking.app.screens.disabled
import com.healthtracking.app.viewmodels.screen.SleepScreenViewModel
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepMain(
    modifier: Modifier,
    viewModel: SleepScreenViewModel
) {
    val sleepEntries by viewModel.pastSleepEntries.observeAsState(emptyList())

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenHeader(
            headerStringId = R.string.track_your_sleep,
            spacerSize = 16.dp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (sleepEntries != null && sleepEntries!!.isNotEmpty()) {
                PastSleepEntries(
                    pastSleepEntries = sleepEntries!!,
                    editSleepEntry = { viewModel.editSleepEntry(it) }
                )
            } else {
                Text(text = stringResource(id = R.string.no_existing_sleep_entries))
            }
        }

        val sleepEntryModifier = if (viewModel.canAddNewEntry) {
            Modifier
        } else {
            Modifier.disabled(colourWeight = 0.7f)
        }
        Column(
            modifier = sleepEntryModifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            AddSleepEntry(
                startTimePickerState = viewModel.startTimePickerState,
                endTimePickerState = viewModel.endTimePickerState,
                rating = viewModel.rating,
                onRatingChange = { viewModel.updateRating(it) },
                saveSleepEntry = { viewModel.saveSleepEntry() },
                canAddNewEntry = viewModel.canAddNewEntry
            )
        }
    }
}

@Composable
fun PastSleepEntries(
    pastSleepEntries: List<Sleep>,
    editSleepEntry: (Long) -> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth(), state = LazyListState(firstVisibleItemIndex = pastSleepEntries.size-1)) {
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
    val hoursSleptDifference = pastSleepEntry.startTime.until(pastSleepEntry.endTime, ChronoUnit.HOURS)
    val hoursSlept = if (hoursSleptDifference < 0) hoursSleptDifference + 24 else hoursSleptDifference

    // maybe add suffixes here
    val formattedDate = pastSleepEntry.date.format(
        DateTimeFormatter.ofPattern("dd MMMM")
    )

    val containerColour = when (pastSleepEntry.rating) {
        1 -> MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
        2 -> MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
        3 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
        4 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
        5 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = {
                openEditSleepEntry()
            }),
        colors = CardDefaults.cardColors(containerColor = containerColour)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Slept $hoursSlept hours",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepEntry(
    startTimePickerState: TimePickerState,
    endTimePickerState: TimePickerState,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    saveSleepEntry: () -> Unit,
    canAddNewEntry: Boolean
) {
    val timeInputColours = TimePickerDefaults.colors(
        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.tertiary,
        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimeInput(state = startTimePickerState, colors = timeInputColours)
        TimeInput(state = endTimePickerState, colors = timeInputColours)
        RatingPicker(rating = rating, onRatingChange = onRatingChange)
        Spacer(modifier = Modifier.height(20.dp))
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

@Composable
fun RatingPicker(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceBetween) {
        SleepRatingButton(
            iconId = R.drawable.large_frown,
            descriptionId = R.string.terrible,
            onClick = { onRatingChange(1) },
            isSelected = rating == 1
        )
        SleepRatingButton(
            iconId = R.drawable.small_frown,
            descriptionId = R.string.bad,
            onClick = { onRatingChange(2) },
            isSelected = rating == 2
        )
        SleepRatingButton(
            iconId = R.drawable.straight_face,
            descriptionId = R.string.ok,
            onClick = { onRatingChange(3) },
            isSelected = rating == 3
        )
        SleepRatingButton(
            iconId = R.drawable.small_smile,
            descriptionId = R.string.good,
            onClick = { onRatingChange(4) },
            isSelected = rating == 4
        )
        SleepRatingButton(
            iconId = R.drawable.large_smile,
            descriptionId = R.string.great,
            onClick = { onRatingChange(5) },
            isSelected = rating == 5
        )
    }
}

@Composable
fun SleepRatingButton(
    iconId: Int,
    descriptionId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(
                color =
                if (isSelected) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.75f)
                else Color.Transparent
            ),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = descriptionId)
        )
    }
}