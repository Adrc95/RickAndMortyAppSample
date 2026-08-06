package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.FavoriteDto

class FavoriteDtoBuilder {
    var color: String = "Red"
    var food: String = "Pizza"
    var random: String = "randomValue"
    var song: String = "Get Schwifty"

    fun withColor(color: String) = apply { this.color = color }
    fun withFood(food: String) = apply { this.food = food }
    fun withRandom(random: String) = apply { this.random = random }
    fun withSong(song: String) = apply { this.song = song }

    fun build() = FavoriteDto(
        color = color,
        food = food,
        random = random,
        song = song,
    )
}

fun favoriteDto(block: FavoriteDtoBuilder.() -> Unit = {}): FavoriteDto =
    FavoriteDtoBuilder().apply(block).build()
