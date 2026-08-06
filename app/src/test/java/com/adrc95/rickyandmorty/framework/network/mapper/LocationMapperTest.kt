package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.framework.network.builder.locationDto
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationMapperTest {

    @Test
    fun `given location dto when mapped to domain then returns location detail with residents count`() {
        // Given
        val dto = locationDto {
            withId(1)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidents(
                listOf(
                    "https://rickandmortyapi.com/api/character/1",
                    "https://rickandmortyapi.com/api/character/2",
                    "https://rickandmortyapi.com/api/character/3"
                )
            )
        }
        val expected = locationDetail {
            withId(1)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidentsCount(3)
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `given location dto with empty residents when mapped to domain then returns zero residents count`() {
        // Given
        val dto = locationDto {
            withResidents(emptyList())
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(0, result.residentsCount)
    }

    @Test
    fun `given location dto with many residents when mapped to domain then returns correct count`() {
        // Given
        val residents = (1..27).map { "https://rickandmortyapi.com/api/character/$it" }
        val dto = locationDto {
            withResidents(residents)
        }

        // When
        val result = dto.toDomain()

        // Then
        assertEquals(27, result.residentsCount)
    }
}
