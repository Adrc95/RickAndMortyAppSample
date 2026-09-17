package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterToEntityMapperTest {

    @Test
    fun given_character_when_mapped_to_entity_then_returns_character_entity() {
        // Given
        val domain = character {
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
        val expected = characterEntity {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withSpecies("Human")
            withType("")
            withGender("Male")
            withOriginName("Earth (C-137)")
            withOriginId(1)
            withLocationName("Citadel of Ricks")
            withLocationId(3)
            withImage("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
            withEpisodeIds(listOf(1, 2))
            withCreated("2017-11-04T18:48:46.250Z")
            withIsFavourite(false)
        }

        // When
        val result = domain.toEntity()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun given_character_with_favourite_when_mapped_to_entity_then_favourite_is_not_preserved() {
        // Given
        val domain = character {
            withIsFavourite(true)
        }

        // When
        val result = domain.toEntity()

        // Then
        assertEquals(false, result.isFavourite)
    }
}
