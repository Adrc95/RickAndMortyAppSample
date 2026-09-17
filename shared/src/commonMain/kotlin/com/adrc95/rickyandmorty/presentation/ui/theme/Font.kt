package com.adrc95.rickyandmorty.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.inter_bold
import com.adrc95.rickyandmorty.generated.resources.inter_medium
import com.adrc95.rickyandmorty.generated.resources.inter_regular
import com.adrc95.rickyandmorty.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun Inter(): FontFamily = FontFamily(
    Font(
        resource = Res.font.inter_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.inter_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.inter_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resource = Res.font.inter_bold,
        weight = FontWeight.Bold
    )
)
