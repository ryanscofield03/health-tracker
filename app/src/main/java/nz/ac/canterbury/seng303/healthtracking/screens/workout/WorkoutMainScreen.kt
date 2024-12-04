package nz.ac.canterbury.seng303.healthtracking.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng303.healthtracking.R

@Composable
fun WorkoutMain (modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.my_workouts_title), style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }

        LazyColumn (modifier = Modifier.fillMaxSize(0.9f)) {
            itemsIndexed (listOf(1, 2, 3)) { index, item ->
                WorkoutCard(Modifier.padding(bottom = 8.dp), id = index)
            }
        }
    }
}

@Composable
fun WorkoutCard(modifier: Modifier, id: Int) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Card(modifier = modifier
        .fillMaxWidth()
        .height(80.dp)) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = "Chest Day", style = MaterialTheme.typography.titleMedium)

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = stringResource(id = R.string.run_workout))
            }

            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = stringResource(id = R.string.view_options))
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource(id = R.string.edit)) }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete)) }, onClick = { /*TODO*/ })
                }
            }
        }
    }
}