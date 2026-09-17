package com.adrc95.rickyandmorty.presentation.filter.mapper

import com.adrc95.rickyandmorty.domain.FilterConstants
import com.adrc95.rickyandmorty.domain.builder.filterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.genders
import com.adrc95.rickyandmorty.generated.resources.species
import com.adrc95.rickyandmorty.generated.resources.status
import kotlin.test.Test
import kotlin.test.assertEquals

class FilterGroupMapperTest {

    @Test
    fun given_species_filter_group_when_mapped_to_display_model_then_returns_species_options() {
        // Given
        val domain = filterGroup {
            withId(FilterConstants.SPECIES_GROUP_ID)
            withOptions(
                listOf(
                    FilterOption(id = "human"),
                    FilterOption(id = "alien"),
                    FilterOption(id = "robot")
                )
            )
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(FilterConstants.SPECIES_GROUP_ID, result.id)
        assertEquals(Res.string.species, result.title)
        assertEquals(
            listOf(
                FilterOptionDisplayModel.Species.Human,
                FilterOptionDisplayModel.Species.Alien,
                FilterOptionDisplayModel.Species.Robot
            ),
            result.options
        )
    }

    @Test
    fun given_gender_filter_group_when_mapped_to_display_model_then_returns_gender_options() {
        // Given
        val domain = filterGroup {
            withId(FilterConstants.GENDER_GROUP_ID)
            withOptions(
                listOf(
                    FilterOption(id = "female"),
                    FilterOption(id = "male"),
                    FilterOption(id = "genderless"),
                    FilterOption(id = "unknown")
                )
            )
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(FilterConstants.GENDER_GROUP_ID, result.id)
        assertEquals(Res.string.genders, result.title)
        assertEquals(
            listOf(
                FilterOptionDisplayModel.Gender.Female,
                FilterOptionDisplayModel.Gender.Male,
                FilterOptionDisplayModel.Gender.Genderless,
                FilterOptionDisplayModel.Gender.Unknown
            ),
            result.options
        )
    }

    @Test
    fun given_status_filter_group_when_mapped_to_display_model_then_returns_status_options() {
        // Given
        val domain = filterGroup {
            withId(FilterConstants.STATUS_GROUP_ID)
            withOptions(
                listOf(
                    FilterOption(id = "alive"),
                    FilterOption(id = "dead"),
                    FilterOption(id = "unknown")
                )
            )
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(FilterConstants.STATUS_GROUP_ID, result.id)
        assertEquals(Res.string.status, result.title)
        assertEquals(
            listOf(
                FilterOptionDisplayModel.Status.Alive,
                FilterOptionDisplayModel.Status.Dead,
                FilterOptionDisplayModel.Status.Unknown
            ),
            result.options
        )
    }

    @Test
    fun given_display_model_species_human_when_mapped_to_domain_then_returns_human_string() {
        // Given
        val display = FilterOptionDisplayModel.Species.Human

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Human", result)
    }

    @Test
    fun given_display_model_gender_female_when_mapped_to_domain_then_returns_female_string() {
        // Given
        val display = FilterOptionDisplayModel.Gender.Female

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Female", result)
    }

    @Test
    fun given_display_model_status_alive_when_mapped_to_domain_then_returns_alive_string() {
        // Given
        val display = FilterOptionDisplayModel.Status.Alive

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Alive", result)
    }

    @Test
    fun given_display_model_species_unknown_when_mapped_to_domain_then_returns_lowercase_unknown() {
        // Given
        val display = FilterOptionDisplayModel.Species.Unknown

        // When
        val result = display.toDomain()

        // Then
        assertEquals("unknown", result)
    }

    @Test
    fun given_display_model_species_mythological_creature_when_mapped_to_domain_then_returns_spaced_string() {
        // Given
        val display = FilterOptionDisplayModel.Species.MythologicalCreature

        // When
        val result = display.toDomain()

        // Then
        assertEquals("Mythological Creature", result)
    }
}
