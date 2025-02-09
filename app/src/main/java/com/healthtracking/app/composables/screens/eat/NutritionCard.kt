package com.healthtracking.app.composables.screens.eat

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.theme.CaloriesColour
import com.healthtracking.app.theme.CarbsColour
import com.healthtracking.app.theme.CustomCutCornerShape
import com.healthtracking.app.theme.FatsColour
import com.healthtracking.app.theme.ProteinColour

/**
 * Card for displaying name, cals, protein, carbs, and fats of a meal/food item
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionCard(
    modifier: Modifier = Modifier,
    title: String,
    calories: Double,
    protein: Double,
    carbs: Double,
    fats: Double,
    onDelete: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        shape = CustomCutCornerShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = onDelete
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val formatter = DecimalFormat("0.#")
                NutrientItem(
                    label = "K",
                    value = "${formatter.format(calories)}kcal",
                    drawableId = R.drawable.calories,
                    iconColour = CaloriesColour
                )
                NutrientItem(
                    label = "P",
                    value = "${formatter.format(protein)}g",
                    drawableId = R.drawable.protein,
                    iconColour = ProteinColour
                )
                NutrientItem(
                    label = "C",
                    value = "${formatter.format(carbs)}g",
                    drawableId = R.drawable.carbs,
                    iconColour = CarbsColour
                )
                NutrientItem(
                    label = "F",
                    value = "${formatter.format(fats)}g",
                    drawableId = R.drawable.fats,
                    iconColour = FatsColour
                )
            }
        }
    }
}

@Composable
private fun NutrientItem(label: String, value: String, drawableId: Int, iconColour: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = label,
            tint = iconColour,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}