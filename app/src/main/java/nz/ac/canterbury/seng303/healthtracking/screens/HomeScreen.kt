package nz.ac.canterbury.seng303.healthtracking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.healthtracking.ui.theme.primaryFont

@Composable
fun Home(navController: NavController){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenCard(title = "Workout", colour = Color(194, 76, 76, 255), onClick = {navController.navigate("Workout")})
        ScreenCard(title = "Eat", colour = Color(93, 52, 168, 255), onClick = {navController.navigate("Eat")})
        ScreenCard(title = "Sleep", colour = Color(66, 170, 77, 255), onClick = {navController.navigate("Sleep")})
    }
}

@Composable
fun ScreenCard(
    title: String,
    colour: Color,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.4f)
                .clip(RoundedCornerShape(80.dp))
                .background(color = colour)
                .border(width = 15.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = primaryFont
                )
            )
        }
    }
}