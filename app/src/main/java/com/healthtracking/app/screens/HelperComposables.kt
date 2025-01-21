package com.healthtracking.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R

@Composable
fun ErrorMessageComponent(hasError: Boolean, errorMessageId: Int?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(5.dp))
        if (hasError) {
            Text(
                text = stringResource(id = errorMessageId!!),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            val lineHeight = with(LocalDensity.current) {
                MaterialTheme.typography.bodySmall.lineHeight.toDp()
            }
            Box(modifier = Modifier.height(lineHeight))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun SaveAndCancelButtons(
    saveButtonLabelId: Int = R.string.save,
    cancelButtonLabelId: Int = R.string.cancel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(stringResource(id = saveButtonLabelId))
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(stringResource(id = cancelButtonLabelId))
            }
        }
    }
}

/**
 * Custom extension to Modifier class for applying disabled colour on content
 * https://stackoverflow.com/questions/75689724/set-the-entire-component-to-grayscale-compose
 */
fun Modifier.disabled(disabledColour: Color, colourWeight: Float): Modifier {
    return drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(Color.Gray, blendMode = BlendMode.Saturation)
        }
    }.graphicsLayer { alpha = colourWeight }
}