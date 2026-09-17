package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.favorite
import com.adrc95.rickyandmorty.framework.network.builder.favoriteDto
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoriteMapperTest {

    @Test
    fun given_favorite_dto_when_mapped_to_domain_then_returns_favorite() {
        // Given
        val dto = favoriteDto {
            withColor("Red")
            withFood("Pizza")
            withRandom("randomValue")
            withSong("Get Schwifty")
        }
        val expected = favorite {
            withColor("Red")
            withFood("Pizza")
            withRandom("randomValue")
            withSong("Get Schwifty")
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun given_favorite_dto_with_different_values_when_mapped_to_domain_then_returns_correct_values() {
        // Given
        val dto = favoriteDto {
            withColor("Blue")
            withFood("Burgers")
            withRandom("anotherRandom")
            withSong("Head Bent Over")
        }
        val expected = favorite {
            withColor("Blue")
            withFood("Burgers")
            withRandom("anotherRandom")
            withSong("Head Bent Over")
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }
}
