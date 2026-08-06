package com.adrc95.rickyandmorty.data.mapper

import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.framework.database.builder.locationDetailEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationDetailToEntityMapperTest {

    @Test
    fun `given location detail when mapped to entity as origin then returns location detail entity`() {
        // Given
        val domain = locationDetail {
            withId(1)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidentsCount(27)
        }
        val expected = locationDetailEntity {
            withId(1)
            withCharacterId(10)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidents(27)
            withIsOrigin(true)
        }

        // When
        val result = domain.toEntity(characterId = 10, isOrigin = true)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `given location detail when mapped to entity as location then is origin is false`() {
        // Given
        val domain = locationDetail {
            withId(3)
        }

        // When
        val result = domain.toEntity(characterId = 10, isOrigin = false)

        // Then
        assertEquals(false, result.isOrigin)
        assertEquals(10, result.characterId)
        assertEquals(3, result.id)
    }

    @Test
    fun `given location detail when mapped to entity then residents count maps to residents`() {
        // Given
        val domain = locationDetail {
            withResidentsCount(42)
        }

        // When
        val result = domain.toEntity(characterId = 1, isOrigin = true)

        // Then
        assertEquals(42, result.residents)
    }
}
