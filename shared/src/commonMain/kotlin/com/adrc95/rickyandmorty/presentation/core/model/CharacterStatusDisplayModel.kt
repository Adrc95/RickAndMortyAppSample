package com.adrc95.rickyandmorty.presentation.core.model

import androidx.compose.ui.graphics.Color
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_ALIVE
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_DEAD
import com.adrc95.rickyandmorty.presentation.ui.theme.BlueGray600
import com.adrc95.rickyandmorty.presentation.ui.theme.Green500
import com.adrc95.rickyandmorty.presentation.ui.theme.Red200
import org.jetbrains.compose.resources.StringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.alive
import com.adrc95.rickyandmorty.generated.resources.dead
import com.adrc95.rickyandmorty.generated.resources.unknown

enum class CharacterStatusDisplayModel(
    val text: StringResource,
    val color: Color
) {
    ALIVE(
        Res.string.alive,
        Green500
    ),
    DEAD(
        Res.string.dead,
        Red200
    ),
    UNKNOWN(
        Res.string.unknown,
        BlueGray600
    );

    companion object {
        fun from(value: String): CharacterStatusDisplayModel = when (value.lowercase()) {
            STATUS_ALIVE -> ALIVE
            STATUS_DEAD -> DEAD
            else -> UNKNOWN
        }
    }
}
