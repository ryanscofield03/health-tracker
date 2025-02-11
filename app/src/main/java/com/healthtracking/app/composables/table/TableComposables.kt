package com.healthtracking.app.composables.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class TableCellType {
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, INTERNAL
}

@Composable
fun RowScope.TableCell(
    type: TableCellType = TableCellType.INTERNAL,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onTertiary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiary
) {
    val cornerRadius = 8.dp
    val shape = when (type) {
        TableCellType.TOP_LEFT -> RoundedCornerShape(topStart = cornerRadius)
        TableCellType.TOP_RIGHT -> RoundedCornerShape(topEnd = cornerRadius)
        TableCellType.BOTTOM_LEFT -> RoundedCornerShape(bottomStart = cornerRadius)
        TableCellType.BOTTOM_RIGHT -> RoundedCornerShape(bottomEnd = cornerRadius)
        TableCellType.INTERNAL -> RoundedCornerShape(0.dp)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .weight(0.25f)
            .height(50.dp)
            .fillMaxSize()
            .clip(shape)
            .background(backgroundColor)
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = text,
            color = textColor
        )
    }
}