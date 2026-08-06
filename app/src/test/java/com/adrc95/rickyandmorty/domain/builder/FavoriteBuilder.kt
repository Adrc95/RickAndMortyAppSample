package com.adrc95.rickyandmorty.domain.builder

import com.adrc95.rickyandmorty.domain.model.Favorite

class FavoriteBuilder {
    var color: String = "Red"
    var food: String = "Pizza"
    var random: String = "randomValue"
    var song: String = "Get Schwifty"

    fun withColor(color: String) = apply { this.color = color }
    fun withFood(food: String) = apply { this.food = food }
    fun withRandom(random: String) = apply { this.random = random }
    fun withSong(song: String) = apply { this.song = song }

    fun build() = Favorite(
        color = color,
        food = food,
        random = random,
        song = song,
    )
}

fun favorite(block: FavoriteBuilder.() -> Unit = {}): Favorite =
    FavoriteBuilder().apply(block).build()
