package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.viewmodels.screen.RunWorkoutViewModel

@Composable
fun RunWorkout(
    modifier: Modifier,
    navController: NavController,
    viewModel: RunWorkoutViewModel
) {
    Column(modifier = modifier) {
        Text(text = viewModel.workout.name)
    }
}