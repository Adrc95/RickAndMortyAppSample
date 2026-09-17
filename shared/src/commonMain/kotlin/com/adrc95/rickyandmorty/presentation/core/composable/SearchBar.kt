package com.adrc95.rickyandmorty.presentation.core.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.presentation.ui.theme.Shapes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.clear_search
import com.adrc95.rickyandmorty.generated.resources.icon_close
import com.adrc95.rickyandmorty.generated.resources.icon_search
import com.adrc95.rickyandmorty.generated.resources.search

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String, onQueryChange: (String) -> Unit,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(text = stringResource(Res.string.search))
        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.icon_search),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_close),
                        contentDescription = stringResource(Res.string.clear_search),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        },
        singleLine = true,
        shape = Shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}
