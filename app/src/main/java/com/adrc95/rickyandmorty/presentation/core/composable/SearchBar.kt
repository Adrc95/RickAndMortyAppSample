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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.ui.theme.Shapes

@Composable
fun SearchBar(modifier: Modifier = Modifier, query: String, onQueryChange: (String) -> Unit, placeholder: String = "") {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(text = stringResource(R.string.search))
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
                painter = painterResource(R.drawable.icon_search),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.icon_close),
                        contentDescription = stringResource(R.string.clear_search),
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
