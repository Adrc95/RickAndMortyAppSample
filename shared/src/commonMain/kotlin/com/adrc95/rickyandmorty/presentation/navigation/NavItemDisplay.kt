package com.adrc95.rickyandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavItemDisplay(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>,
    entryProvider: (NavKey) -> NavEntry<NavKey>
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider
    )
}
