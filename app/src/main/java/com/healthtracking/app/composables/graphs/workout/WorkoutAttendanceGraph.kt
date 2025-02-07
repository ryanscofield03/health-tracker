package com.healthtracking.app.composables.graphs.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ContentInfoCompat.Flags
import com.healthtracking.app.entities.WorkoutHistory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutAttendanceGraph(
    workoutData: List<WorkoutHistory>,
    workoutAttendance: Float
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AttendanceData(
            workoutAttendance = workoutAttendance
        )

        WorkoutCards(
            workoutData = workoutData
        )
    }
}

@Composable
private fun WorkoutCards(
    workoutData: List<WorkoutHistory>
) {
    val dateList: List<LocalDate> = generateLocalDates(data = workoutData)
    val workoutMap = workoutData.groupBy { it.date }

    LazyRow(state = rememberLazyListState(initialFirstVisibleItemIndex = dateList.size - 1)) {
        items(dateList) { date ->
            val workoutForDate = workoutMap[date]

            WorkoutHistoryCard(
                date = date,
                workoutHistory = workoutForDate
            )
        }
    }
}

private fun generateLocalDates(data: List<WorkoutHistory>): List<LocalDate> {
    val uniqueDays = data.map { it.date }.toSet()
    val daysToAdd = if (uniqueDays.size < 7) 7 - uniqueDays.size else 0
    val startDate = data.firstOrNull()?.date?.minusDays(daysToAdd.toLong()) ?: LocalDate.now().minusDays(7)

    val listOfDates: List<LocalDate> = generateSequence(startDate) { it.plusDays(1) }
        .takeWhile { it <= (data.lastOrNull()?.date ?: LocalDate.now()) }
        .toList()

    return listOfDates
}

@Composable
private fun WorkoutHistoryCard(
    date: LocalDate,
    workoutHistory: List<WorkoutHistory>?
) {
    val cardColor = if (workoutHistory != null) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (workoutHistory != null) MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = date.format(DateTimeFormatter.ofPattern("dd MMM yy")),
                color = textColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (!workoutHistory.isNullOrEmpty()) {
                workoutHistory.forEach { workout ->
                    Text(
                        text = workout.name,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "Rest",
                    color = textColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun AttendanceData(
    workoutAttendance: Float
) {
    Text(text = "Workout Attendance: $workoutAttendance days/week")
}
