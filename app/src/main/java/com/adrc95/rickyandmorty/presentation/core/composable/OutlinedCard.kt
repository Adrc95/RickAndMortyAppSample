package com.adrc95.rickyandmorty.presentation.core.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.presentation.ui.theme.Shapes

@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = content,
    )
}
