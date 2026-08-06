package com.adrc95.rickyandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.adrc95.rickyandmorty.presentation.detail.DetailRoute
import com.adrc95.rickyandmorty.presentation.detail.DetailViewModel
import com.adrc95.rickyandmorty.presentation.home.HomeRoute
import com.adrc95.rickyandmorty.presentation.settings.SettingsRoute

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Route.Home)
    NavItemDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider { AppEntryProvider(backStack = backStack) }
    )
}

@Composable
private fun EntryProviderScope<NavKey>.AppEntryProvider(backStack: NavBackStack<NavKey>) {
    entry<Route.Home> {
        HomeRoute(
            onCharacterClick = { item -> backStack.add(Route.Detail(item.id)) },
            onSettingsClick = { backStack.add(Route.Settings) }
        )
    }
    entry<Route.Detail> { route ->
        val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(route)
            }
        )
        DetailRoute(
            viewModel = viewModel,
            onSettingsClick = { backStack.add(Route.Settings) },
            onBack = { backStack.removeLastOrNull() }
        )
    }
    entry<Route.Settings> {
        SettingsRoute(
            onBack = { backStack.removeLastOrNull() }
        )
    }
}
