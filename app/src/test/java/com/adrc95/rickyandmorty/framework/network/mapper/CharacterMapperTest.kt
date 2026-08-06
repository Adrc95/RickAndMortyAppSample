package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.framework.network.builder.characterDto
import com.adrc95.rickyandmorty.framework.network.builder.summaryLocationDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterMapperTest {

    @Test
    fun `given character dto when mapped to domain then returns character`() {
        // Given
        val dto = characterDto {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withSpecies("Human")
            withType("")
            withGender("Male")
            withOrigin(summaryLocationDto {
                withName("Earth (C-137)")
                withUrl("https://rickandmortyapi.com/api/location/1")
            })
            withLocation(summaryLocationDto {
                withName("Citadel of Ricks")
                withUrl("https://rickandmortyapi.com/api/location/3")
            })
            withImage("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
            withEpisode(listOf(
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/2"
            ))
            withCreated("2017-11-04T18:48:46.250Z")
        }
        val expected = character {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withSpecies("Human")
            withType("")
            withGender("Male")
            withOrigin(summaryLocation { withId(1); withName("Earth (C-137)") })
            withLocation(summaryLocation { withId(3); withName("Citadel of Ricks") })
            withImage("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
            withEpisodeIds(listOf(1, 2))
            withCreated("2017-11-04T18:48:46.250Z")
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `given character dto with invalid episode urls when mapped to domain then returns minus one ids`() {
        // Given
        val dto = characterDto {
            withEpisode(listOf("invalid-url", "https://rickandmortyapi.com/api/episode/abc"))
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(listOf(-1, -1), result.episodeIds)
    }

    @Test
    fun `given character dto with empty episode list when mapped to domain then returns empty list`() {
        // Given
        val dto = characterDto {
            withEpisode(emptyList())
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(emptyList<Int>(), result.episodeIds)
    }

    @Test
    fun `given summary location dto when mapped to domain then returns summary location`() {
        // Given
        val dto = summaryLocationDto {
            withName("Earth (C-137)")
            withUrl("https://rickandmortyapi.com/api/location/1")
        }
        val expected = summaryLocation {
            withId(1)
            withName("Earth (C-137)")
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `given summary location dto with invalid url when mapped to domain then returns minus one id`() {
        // Given
        val dto = summaryLocationDto {
            withUrl("invalid-url")
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(-1, result.id)
    }

    @Test
    fun `given character dto isFavourite defaults to false`() {
        // Given
        val dto = characterDto()

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(false, result.isFavourite)
    }
}
