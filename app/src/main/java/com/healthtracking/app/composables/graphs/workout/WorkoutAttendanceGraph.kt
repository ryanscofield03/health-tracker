package com.healthtracking.app.composables.graphs.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.healthtracking.app.entities.WorkoutHistory

@Composable
fun WorkoutAttendanceGraph(
    workoutData: List<WorkoutHistory>,
    workoutAttendance: Double
) {
    Column(

    ) {
        AttendanceCards(

        )

        AttendanceData(

        )
    }
}

@Composable
fun AttendanceCards(

) {
}

@Composable
fun AttendanceData(

) {
}
