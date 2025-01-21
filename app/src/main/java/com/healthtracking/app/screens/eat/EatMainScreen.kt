package com.healthtracking.app.screens.eat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthtracking.app.R
import com.healthtracking.app.screens.ScreenHeader

@Composable
fun EatMain (
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            headerStringId = R.string.eat_title,
            spacerSize = 16.dp
        )
    }
}