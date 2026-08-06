package com.adrc95.rickyandmorty.presentation.filter.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T : FilterOptionDisplayModel> FilterSection(
    @StringRes title: Int,
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit
) {
    Column {
        Text(
            text = stringResource(title).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
            modifier = Modifier.semantics { heading() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selected
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(option) },
                    modifier = Modifier.heightIn(min = 48.dp),
                    label = { Text(text = stringResource(option.label)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.onSurface,
                        selectedLabelColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = MaterialTheme.colorScheme.onSurface,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }
    }
}
