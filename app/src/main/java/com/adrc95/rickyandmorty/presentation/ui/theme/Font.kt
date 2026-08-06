package com.adrc95.rickyandmorty.presentation.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.adrc95.rickyandmorty.R

val Inter = FontFamily(
    Font(
        resId = R.font.inter_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.inter_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.inter_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.inter_bold,
        weight = FontWeight.Bold
    )
)
