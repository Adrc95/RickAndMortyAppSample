package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.favorite
import com.adrc95.rickyandmorty.framework.network.builder.favoriteDto
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMapperTest {

    @Test
    fun `given favorite dto when mapped to domain then returns favorite`() {
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
    fun `given favorite dto with different values when mapped to domain then returns correct values`() {
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
