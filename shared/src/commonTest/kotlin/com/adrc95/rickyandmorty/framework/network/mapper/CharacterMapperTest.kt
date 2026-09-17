package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.framework.network.builder.characterDto
import com.adrc95.rickyandmorty.framework.network.builder.summaryLocationDto
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterMapperTest {

    @Test
    fun given_character_dto_when_mapped_to_domain_then_returns_character() {
        // Given
        val dto = characterDto {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withSpecies("Human")
            withType("")
            withGender("Male")
            withOrigin(
                summaryLocationDto {
                    withName("Earth (C-137)")
                    withUrl("https://rickandmortyapi.com/api/location/1")
                }
            )
            withLocation(
                summaryLocationDto {
                    withName("Citadel of Ricks")
                    withUrl("https://rickandmortyapi.com/api/location/3")
                }
            )
            withImage("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
            withEpisode(
                listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2"
                )
            )
            withCreated("2017-11-04T18:48:46.250Z")
        }
        val expected = character {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withSpecies("Human")
            withType("")
            withGender("Male")
            withOrigin(
                summaryLocation {
                    withId(1)
                    withName("Earth (C-137)")
                }
            )
            withLocation(
                summaryLocation {
                    withId(3)
                    withName("Citadel of Ricks")
                }
            )
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
    fun given_character_dto_with_invalid_episode_urls_when_mapped_to_domain_then_returns_minus_one_ids() {
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
    fun given_character_dto_with_empty_episode_list_when_mapped_to_domain_then_returns_empty_list() {
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
    fun given_summary_location_dto_when_mapped_to_domain_then_returns_summary_location() {
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
    fun given_summary_location_dto_with_invalid_url_when_mapped_to_domain_then_returns_minus_one_id() {
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
    fun given_character_dto_isFavourite_defaults_to_false() {
        // Given
        val dto = characterDto()

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(false, result.isFavourite)
    }
}
