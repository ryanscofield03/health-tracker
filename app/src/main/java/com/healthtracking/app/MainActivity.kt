package com.healthtracking.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.screens.eat.EatMain
import com.healthtracking.app.screens.Welcome
import com.healthtracking.app.screens.settings.SettingsMain
import com.healthtracking.app.screens.sleep.SleepMain
import com.healthtracking.app.screens.stats.StatsMain
import com.healthtracking.app.screens.workout.AddExercise
import com.healthtracking.app.screens.workout.BuildWorkout
import com.healthtracking.app.screens.workout.RunWorkout
import com.healthtracking.app.screens.workout.ScheduleWorkout
import com.healthtracking.app.screens.workout.WorkoutMain
import com.healthtracking.app.ui.theme.HealthTrackingTheme
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
import com.healthtracking.app.viewmodels.screen.AddWorkoutViewModel
import com.healthtracking.app.viewmodels.screen.RunWorkoutViewModel
import com.healthtracking.app.viewmodels.screen.SettingsViewModel
import com.healthtracking.app.viewmodels.screen.SleepScreenViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

data class TabBarItem (
    val title: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
    private val workoutViewModel: WorkoutViewModel by koinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthTrackingTheme {
                val navController = rememberNavController()
                val addWorkoutViewModel: AddWorkoutViewModel by koinViewModel()

                val workoutTab = TabBarItem(
                    title = stringResource(R.string.workout_screen),
                    icon = ImageVector.vectorResource(id = R.drawable.workout)
                )
                val eatTab = TabBarItem(
                    title = stringResource(R.string.eat_screen),
                    icon = ImageVector.vectorResource(id = R.drawable.eat)
                )
                val sleepTab = TabBarItem(
                    title = stringResource(R.string.sleep_screen),
                    icon = ImageVector.vectorResource(id = R.drawable.sleep)
                )
                val historyTab = TabBarItem(
                    title = stringResource(R.string.stats_screen),
                    icon = ImageVector.vectorResource(id = R.drawable.stats)
                )
                val settingsTab = TabBarItem(
                    title = stringResource(R.string.settings_screen),
                    icon = Icons.Filled.Settings
                )
                val tabBarItems: List<TabBarItem> = listOf(workoutTab, eatTab, sleepTab, historyTab, settingsTab)

                var selectedTabIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                    if (currentDestination != stringResource(R.string.welcome_screen)) {
                        NavigationBar {
                            tabBarItems.forEachIndexed {index, item ->
                                NavigationBarItem(
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                                    ),
                                    label = { Text(text = item.title, style = MaterialTheme.typography.labelSmall)},
                                    selected = selectedTabIndex == index,
                                    onClick = { navController.navigate(item.title); selectedTabIndex = index },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = title.toString()
                                        )
                                    }
                                )
                            }
                        }
                    }
                }) { innerPadding ->
                    val padding = PaddingValues(8.dp, 48.dp, 8.dp, 128.dp)
                    NavHost(
                        navController = navController,
                        startDestination = "Welcome",
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        composable("Welcome") {
                            Welcome(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        composable(route = "Workout") {
                            WorkoutMain(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                workoutViewModel = workoutViewModel
                            )
                        }
                        composable(route = "AddWorkout") {
                            BuildWorkout(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = addWorkoutViewModel
                            )
                        }
                        composable(route = "EditWorkout/{id}") { navBackStackEntry ->
                            // get id, get workout, insert workout data into viewmodel for screen
                            val parsedId = navBackStackEntry.arguments?.getString("id")?.toLong()
                            val workout = workoutViewModel
                                .allWorkouts
                                .value
                                ?.find { it.id == parsedId }
                            workout?.let { addWorkoutViewModel.addWorkoutInfo(workout = it) }

                            BuildWorkout(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = addWorkoutViewModel,
                            )
                        }
                        composable(route = "AddExercise") {
                            AddExercise(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = addWorkoutViewModel
                            )
                        }
                        composable(route = "ScheduleWorkout") {
                            ScheduleWorkout(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = addWorkoutViewModel
                            )
                        }
                        composable(route = "RunWorkout/{id}") { navBackStackEntry ->
                            val parsedId = navBackStackEntry.arguments?.getString("id")?.toLong()
                            val workout = workoutViewModel
                                .allWorkouts
                                .value
                                ?.find { it.id == parsedId }

                            if (workout != null) {
                                var exercises by remember { mutableStateOf<List<Exercise>?>(null) }
                                LaunchedEffect(workout) {
                                    exercises = workoutViewModel.getExercisesForWorkout(workoutId = workout.id)
                                }

                                if (exercises != null) {
                                    val runWorkoutViewModel: RunWorkoutViewModel = getViewModel(
                                        parameters = { parametersOf(workout, exercises) }
                                    )

                                    RunWorkout(
                                        modifier = Modifier.padding(padding),
                                        navController = navController,
                                        viewModel = runWorkoutViewModel
                                    )
                                }
                            }
                        }
                        composable("Eat"){ EatMain() }
                        composable("Sleep"){
                            val sleepScreenViewModel: SleepScreenViewModel by koinViewModel()
                            SleepMain(
                                modifier = Modifier.padding(padding),
                                viewModel = sleepScreenViewModel
                            )
                        }
                        composable("Stats"){ StatsMain() }
                        composable("Settings") {
                            val settingsViewModel: SettingsViewModel by koinViewModel()
                            SettingsMain(
                                modifier = Modifier.padding(padding),
                                viewModel = settingsViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}