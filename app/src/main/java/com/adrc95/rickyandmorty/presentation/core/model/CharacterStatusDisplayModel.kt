package com.adrc95.rickyandmorty.presentation.core.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_ALIVE
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_DEAD
import com.adrc95.rickyandmorty.presentation.ui.theme.BlueGray600
import com.adrc95.rickyandmorty.presentation.ui.theme.Green500
import com.adrc95.rickyandmorty.presentation.ui.theme.Red200

enum class CharacterStatusDisplayModel(
    @StringRes val text: Int,
    val color: Color
) {
    ALIVE(
        R.string.alive,
        Green500
    ),
    DEAD(
        R.string.dead,
        Red200
    ),
    UNKNOWN(
        R.string.unknown,
        BlueGray600
    );

    companion object {
        fun from(value: String): CharacterStatusDisplayModel =
            when (value.lowercase()) {
                STATUS_ALIVE -> ALIVE
                STATUS_DEAD -> DEAD
                else -> UNKNOWN
            }
    }
}