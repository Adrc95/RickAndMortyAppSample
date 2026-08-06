package com.adrc95.rickyandmorty.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.composable.AppToolbar
import com.adrc95.rickyandmorty.presentation.core.composable.OutlinedCard
import com.adrc95.rickyandmorty.presentation.settings.composable.ThemeOption
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val themeMode by viewModel.themeMode.collectAsState()
    SettingsScreen(
        themeMode = themeMode,
        onThemeModeSelected = viewModel::onThemeModeSelected,
        onBack = onBack,
    )
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    themeMode: ThemeModeDisplayModel,
    onThemeModeSelected: (ThemeModeDisplayModel) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings),
                onNavigationClick = onBack,
            )
        }
    ) { padding ->
        SettingContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            themeMode = themeMode,
            onThemeModeSelected = onThemeModeSelected,
        )
    }
}

@Composable
private fun SettingContent(
    modifier: Modifier = Modifier,
    themeMode: ThemeModeDisplayModel,
    onThemeModeSelected: (ThemeModeDisplayModel) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.appearance),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 24.dp
                )
                .semantics { heading() }
        )
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
            ) {
                ThemeOption(
                    label = stringResource(ThemeModeDisplayModel.LIGHT.text),
                    icon = ThemeModeDisplayModel.LIGHT.icon,
                    selected = themeMode == ThemeModeDisplayModel.LIGHT,
                    onClick = { onThemeModeSelected(ThemeModeDisplayModel.LIGHT) }
                )
                ThemeOption(
                    label = stringResource(ThemeModeDisplayModel.DARK.text),
                    icon = ThemeModeDisplayModel.DARK.icon,
                    selected = themeMode == ThemeModeDisplayModel.DARK,
                    onClick = { onThemeModeSelected(ThemeModeDisplayModel.DARK) }
                )
                ThemeOption(
                    label = stringResource(ThemeModeDisplayModel.SYSTEM.text),
                    icon = ThemeModeDisplayModel.SYSTEM.icon,
                    selected = themeMode == ThemeModeDisplayModel.SYSTEM,
                    onClick = { onThemeModeSelected(ThemeModeDisplayModel.SYSTEM) }
                )
            }
        }
    }
}
