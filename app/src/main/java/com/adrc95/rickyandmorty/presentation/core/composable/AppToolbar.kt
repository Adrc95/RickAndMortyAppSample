package com.adrc95.rickyandmorty.presentation.core.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.TestTags.BACK_BUTTON
import com.adrc95.rickyandmorty.presentation.core.TestTags.SETTINGS_BUTTON

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    titleColor: Color = Color.Unspecified,
    iconColor: Color = MaterialTheme.colorScheme.primary,
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            navigationIcon = {
                if (onNavigationClick != null) {
                    IconButton(
                        modifier = Modifier.testTag(BACK_BUTTON),
                        onClick = onNavigationClick,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_back),
                            contentDescription = stringResource(R.string.back),
                            tint = iconColor,
                        )
                    }
                }
            },
            actions = {
                if (onActionClick != null) {
                    IconButton(
                        modifier = Modifier.testTag(SETTINGS_BUTTON),
                        onClick = onActionClick,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_settings),
                            contentDescription = stringResource(R.string.settings),
                            tint = iconColor,
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                titleContentColor = titleColor,
                navigationIconContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified,
            ),
        )
        if (showDivider) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
