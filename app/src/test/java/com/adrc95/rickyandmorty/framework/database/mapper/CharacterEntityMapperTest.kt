package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterEntityMapperTest {

    @Test
    fun `given character entity when mapped to domain then returns character`() {
        // Given
        val entity = characterEntity {
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
            withIsFavourite(true)
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
            withIsFavourite(true)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `given character entity with null origin id when mapped to domain then returns minus one`() {
        // Given
        val entity = characterEntity {
            withOriginId(null)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(-1, result.origin.id)
    }

    @Test
    fun `given character entity with null location id when mapped to domain then returns minus one`() {
        // Given
        val entity = characterEntity {
            withLocationId(null)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(-1, result.location.id)
    }

    @Test
    fun `given character entity with favourite true when mapped to domain then preserves favourite`() {
        // Given
        val entity = characterEntity {
            withIsFavourite(true)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(true, result.isFavourite)
    }
}
