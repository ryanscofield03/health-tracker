package com.healthtracking.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.healthtracking.app.composables.screens.eat.BuildMeal
import com.healthtracking.app.entities.Exercise
import com.healthtracking.app.composables.screens.eat.EatMain
import com.healthtracking.app.composables.screens.settings.SettingsMain
import com.healthtracking.app.composables.screens.sleep.SleepMain
import com.healthtracking.app.composables.screens.stats.StatsMain
import com.healthtracking.app.composables.screens.workout.AddExercise
import com.healthtracking.app.composables.screens.workout.BuildWorkout
import com.healthtracking.app.composables.screens.workout.RunWorkout
import com.healthtracking.app.composables.screens.workout.ScheduleWorkout
import com.healthtracking.app.composables.screens.workout.WorkoutMain
import com.healthtracking.app.theme.HealthTrackingTheme
import com.healthtracking.app.viewmodels.database.MealViewModel
import com.healthtracking.app.viewmodels.database.WorkoutViewModel
import com.healthtracking.app.viewmodels.screen.BuildWorkoutViewModel
import com.healthtracking.app.viewmodels.screen.BuildMealViewModel
import com.healthtracking.app.viewmodels.screen.FoodViewModel
import com.healthtracking.app.viewmodels.screen.RunWorkoutViewModel
import com.healthtracking.app.viewmodels.database.SettingsViewModel
import com.healthtracking.app.viewmodels.screen.SleepScreenViewModel
import com.healthtracking.app.viewmodels.screen.StatsScreenViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

data class TabBarItem (
    val title: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
    private val workoutViewModel: WorkoutViewModel by koinViewModel()
    private val mealViewModel: MealViewModel by koinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthTrackingTheme {
                val navController = rememberNavController()
                val addWorkoutViewModel: BuildWorkoutViewModel by koinViewModel()
                val buildMealViewModel: BuildMealViewModel by koinViewModel()

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
                val navSuiteType = with(LocalConfiguration.current) {
                    if (screenHeightDp >= 600) {
                        NavigationSuiteType.NavigationBar
                    } else {
                        NavigationSuiteType.NavigationRail
                    }
                }

                val navBarItemColours = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onTertiary,
                    selectedTextColor = MaterialTheme.colorScheme.onTertiary,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
                val navRailItemColours = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onTertiary,
                    selectedTextColor = MaterialTheme.colorScheme.onTertiary,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
                val navDrawerColours = NavigationDrawerItemDefaults.colors()

                val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }
                NavigationSuiteScaffold(
                    layoutType = navSuiteType,
                    navigationSuiteColors = NavigationSuiteDefaults.colors(
                        navigationBarContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                        navigationRailContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                    ),
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationSuiteItems = {
                        tabBarItems.forEachIndexed { index, navItem ->
                            item(
                                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.title) },
                                label = { Text(navItem.title) },
                                selected = selectedTabIndex.intValue == index,
                                onClick = {
                                    if (selectedTabIndex.intValue != index) {
                                        navController.navigate(navItem.title)
                                        selectedTabIndex.intValue = index
                                    }
                                },
                                colors = NavigationSuiteItemColors(
                                    navigationBarItemColors = navBarItemColours,
                                    navigationRailItemColors = navRailItemColours,
                                    navigationDrawerItemColors = navDrawerColours
                                )
                            )
                        }
                    }
                ) {
                    val padding = PaddingValues(
                        start = 24.dp,
                        top = 40.dp,
                        end = 24.dp,
                        bottom = 16.dp
                    )

                    val routeList = listOf("Workout", "Eat", "Sleep", "Progress", "Settings")

                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "Workout",
                        enterTransition = {
                            val initialRoute = navController.previousBackStackEntry?.destination?.route
                            val destinationRoute = navController.currentBackStackEntry?.destination?.route

                            val initialRouteIndex = routeList.indexOf(initialRoute)
                            val destinationRouteIndex = routeList.indexOf(destinationRoute)

                            if (!routeList.contains(destinationRoute) || !routeList.contains(initialRoute)) {
                                // slide up
                                slideInVertically( initialOffsetY = { 1000 }) + fadeIn()
                            } else if (destinationRouteIndex > initialRouteIndex) {
                                // moving right
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            } else {
                                // moving left
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            }
                        },
                        exitTransition = {
                            val initialRoute = navController.previousBackStackEntry?.destination?.route
                            val destinationRoute = navController.currentBackStackEntry?.destination?.route

                            val initialRouteIndex = routeList.indexOf(initialRoute)
                            val destinationRouteIndex = routeList.indexOf(destinationRoute)

                            if (!routeList.contains(destinationRoute) || !routeList.contains(initialRoute)) {
                                // slide up
                                slideOutVertically( targetOffsetY = { -1000 }) + fadeOut()
                            } else if (destinationRouteIndex > initialRouteIndex) {
                                // moving right
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            } else {
                                // moving left
                                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                            }
                        }
                    ) {
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
                                .collectAsStateWithLifecycle()
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
                                .collectAsStateWithLifecycle()
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
                        composable("Eat"){
                            val mealViewModel: FoodViewModel by koinViewModel()
                            EatMain(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = mealViewModel
                            )
                        }
                        composable(route = "AddMeal") {
                            buildMealViewModel.clear()
                            BuildMeal(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = buildMealViewModel
                            )
                        }
                        composable(route = "EditMeal/{id}") { navBackStackEntry ->
                            // get id, get workout, insert workout data into viewmodel for screen
                            val parsedId = navBackStackEntry.arguments?.getString("id")?.toLong()
                            val mealWithFoodList = mealViewModel
                                .allMeals
                                .collectAsStateWithLifecycle(listOf())
                                .value
                                ?.find { it.meal.id == parsedId }
                            mealWithFoodList?.let { buildMealViewModel.editMealInfo(mealWithFoodList = it) }

                            BuildMeal(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                viewModel = buildMealViewModel,
                            )
                        }
                        composable("Sleep"){
                            val sleepScreenViewModel: SleepScreenViewModel by koinViewModel()
                            SleepMain(
                                modifier = Modifier.padding(padding),
                                viewModel = sleepScreenViewModel
                            )
                        }
                        composable("Progress"){
                            val statsScreenViewModel: StatsScreenViewModel by koinViewModel()
                            StatsMain(
                                modifier = Modifier.padding(padding),
                                viewModel = statsScreenViewModel
                            )
                        }
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