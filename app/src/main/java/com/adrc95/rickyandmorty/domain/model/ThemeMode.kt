package com.adrc95.rickyandmorty.domain.model

enum class ThemeMode(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun from(value: String): ThemeMode = entries.firstOrNull { it.value == value } ?: SYSTEM
    }
}
