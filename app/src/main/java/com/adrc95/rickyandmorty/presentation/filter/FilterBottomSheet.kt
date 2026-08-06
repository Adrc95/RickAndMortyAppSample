package com.adrc95.rickyandmorty.presentation.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_GROUP_ID
import com.adrc95.rickyandmorty.presentation.filter.composable.FilterSection
import com.adrc95.rickyandmorty.presentation.filter.model.FilterGroupDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filterGroupDisplayModels: List<FilterGroupDisplayModel>,
    currentFilters: CharacterFiltersDisplayModel,
    onApply: (CharacterFiltersDisplayModel) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    val selectedMap = remember(currentFilters) {
        mutableStateMapOf<String, FilterOptionDisplayModel?>().apply {
            filterGroupDisplayModels.forEach { group ->
                val option = when (group.id) {
                    SPECIES_GROUP_ID -> currentFilters.species
                    GENDER_GROUP_ID -> currentFilters.gender
                    STATUS_GROUP_ID -> currentFilters.status
                    else -> null
                }
                put(group.id, option)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(R.string.filter_character),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
            )

            filterGroupDisplayModels.forEach { group ->
                FilterSection(
                    title = group.title,
                    options = group.options,
                    selected = selectedMap[group.id],
                    onSelect = { option ->
                        selectedMap[group.id] =
                            if (option == selectedMap[group.id]) null else option
                    },
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                onApply(
                    CharacterFiltersDisplayModel(
                        species = selectedMap[SPECIES_GROUP_ID]
                                as? FilterOptionDisplayModel.Species,
                        gender = selectedMap[GENDER_GROUP_ID]
                                as? FilterOptionDisplayModel.Gender,
                        status = selectedMap[STATUS_GROUP_ID]
                                as? FilterOptionDisplayModel.Status,
                    )
                )
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                )
                .height(48.dp),
            shape = Shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                filterGroupDisplayModels.forEach { group -> selectedMap[group.id] = null }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                ),
        ) {
            Text(
                text = stringResource(R.string.clear_filters),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
