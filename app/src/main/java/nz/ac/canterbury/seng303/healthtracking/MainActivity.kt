package nz.ac.canterbury.seng303.healthtracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.ac.canterbury.seng303.healthtracking.screens.Home
import nz.ac.canterbury.seng303.healthtracking.screens.WelcomeScreen
import nz.ac.canterbury.seng303.healthtracking.ui.theme.HealthTrackingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthTrackingTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Welcome",
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        composable("Welcome") {
                            WelcomeScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        composable("Home") {
                            Home(navController = navController)
                        }
                    }
                }
            }
        }
    }
}