package com.healthtracking.app.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

@Composable
fun SelectionDropDown(
    items: List<String>,
    selectedText: String,
    onItemClick: (String) -> Unit
) {
    val expanded = rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Button(
            onClick = { expanded.value = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = selectedText,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Icon",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(MaterialTheme.colorScheme.surface),
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text( text = item ) },
                    onClick = { onItemClick(item); expanded.value = false }
                )
            }
        }
    }
}

/**
 * Content wrapper to make a dim background with padding
 */
@Composable
fun BackgroundBorderBox(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.surfaceDim,
            shape = RoundedCornerShape(10.dp)
        )
        .padding(12.dp)
    ) {
        content()
    }
}