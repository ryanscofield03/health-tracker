package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng303.healthtracking.R

@Composable
fun WorkoutMain (modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Text(text = stringResource(R.string.my_workouts_title), style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }

        LazyColumn (modifier = Modifier.fillMaxSize(0.9f)) {
            item {
                WorkoutCard()
            }
            item {
                WorkoutCard()
            }
            item {
                WorkoutCard()
            }
            item {
                WorkoutCard()
            }
            item {
                WorkoutCard()
            }
        }
    }
}

@Composable
fun WorkoutCard() {
    Card (modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)) {
        Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = "Chest Day", style = MaterialTheme.typography.titleMedium)

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = stringResource(id = R.string.run_workout))
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = stringResource(id = R.string.view_options))
            }
        }
    }
}