package com.healthtracking.app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R

@Composable
fun TextFieldWithErrorMessage(
    value: String?,
    onValueChange: (String) -> Unit,
    labelId: Int,
    placeholderId: Int,
    hasError: Boolean,
    errorMessageId: Int?
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value ?: "",
        onValueChange = { onValueChange(it) },
        label = { Text(stringResource(id = labelId)) },
        placeholder = { Text(stringResource(id = placeholderId)) },
        maxLines = 1,
        isError = hasError,
        colors = TextFieldDefaults.colors(
            errorContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
        )
    )
    ErrorMessageComponent(hasError = hasError, errorMessageId = errorMessageId)
}

@Composable
fun ErrorMessageComponent(hasError: Boolean, errorMessageId: Int?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(5.dp))
        AnimatedVisibility(
            visible = hasError,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 })
        ) {
            Text(
                text = stringResource(id = errorMessageId ?: R.string.cannot_find_error_message),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
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
fun Modifier.disabled(colourWeight: Float): Modifier {
    return drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(Color.Gray, blendMode = BlendMode.Saturation)
        }
    }.graphicsLayer { alpha = colourWeight }
}

@Composable
fun ScreenHeader(
    headerStringId: Int,
    spacerSize: Dp = 12.dp
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = headerStringId),
            style = MaterialTheme.typography.displaySmall
        )
    }

    Spacer(modifier = Modifier.height(spacerSize))
    HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
    Spacer(modifier = Modifier.height(spacerSize))
}