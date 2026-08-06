package com.adrc95.rickyandmorty.presentation.filter.mapper

import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.builder.filterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption
import com.adrc95.rickyandmorty.domain.usecase.GetFilterGroupsUseCase
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterGroupMapperTest {

    @Test
    fun `given species filter group when mapped to display model then returns species options`() {
        // Given
        val domain = filterGroup {
            withId(GetFilterGroupsUseCase.SPECIES_GROUP_ID)
            withOptions(listOf(
                FilterOption(id = "human"),
                FilterOption(id = "alien"),
                FilterOption(id = "robot"),
            ))
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(GetFilterGroupsUseCase.SPECIES_GROUP_ID, result.id)
        assertEquals(R.string.species, result.title)
        assertEquals(listOf(
            FilterOptionDisplayModel.Species.Human,
            FilterOptionDisplayModel.Species.Alien,
            FilterOptionDisplayModel.Species.Robot,
        ), result.options)
    }

    @Test
    fun `given gender filter group when mapped to display model then returns gender options`() {
        // Given
        val domain = filterGroup {
            withId(GetFilterGroupsUseCase.GENDER_GROUP_ID)
            withOptions(listOf(
                FilterOption(id = "female"),
                FilterOption(id = "male"),
                FilterOption(id = "genderless"),
                FilterOption(id = "unknown"),
            ))
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(GetFilterGroupsUseCase.GENDER_GROUP_ID, result.id)
        assertEquals(R.string.genders, result.title)
        assertEquals(listOf(
            FilterOptionDisplayModel.Gender.Female,
            FilterOptionDisplayModel.Gender.Male,
            FilterOptionDisplayModel.Gender.Genderless,
            FilterOptionDisplayModel.Gender.Unknown,
        ), result.options)
    }

    @Test
    fun `given status filter group when mapped to display model then returns status options`() {
        // Given
        val domain = filterGroup {
            withId(GetFilterGroupsUseCase.STATUS_GROUP_ID)
            withOptions(listOf(
                FilterOption(id = "alive"),
                FilterOption(id = "dead"),
                FilterOption(id = "unknown"),
            ))
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(GetFilterGroupsUseCase.STATUS_GROUP_ID, result.id)
        assertEquals(R.string.status, result.title)
        assertEquals(listOf(
            FilterOptionDisplayModel.Status.Alive,
            FilterOptionDisplayModel.Status.Dead,
            FilterOptionDisplayModel.Status.Unknown,
        ), result.options)
    }

    @Test
    fun `given display model species human when mapped to domain then returns human string`() {
        // Given
        val display = FilterOptionDisplayModel.Species.Human

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Human", result)
    }

    @Test
    fun `given display model gender female when mapped to domain then returns female string`() {
        // Given
        val display = FilterOptionDisplayModel.Gender.Female

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Female", result)
    }

    @Test
    fun `given display model status alive when mapped to domain then returns alive string`() {
        // Given
        val display = FilterOptionDisplayModel.Status.Alive

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Alive", result)
    }

    @Test
    fun `given display model species unknown when mapped to domain then returns lowercase unknown`() {
        // Given
        val display = FilterOptionDisplayModel.Species.Unknown

        // When
        val result = display.toDomain()

        // Then
        assertEquals("unknown", result)
    }

    @Test
    fun `given display model species mythological creature when mapped to domain then returns spaced string`() {
        // Given
        val display = FilterOptionDisplayModel.Species.MythologicalCreature

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Mythological Creature", result)
    }
}
