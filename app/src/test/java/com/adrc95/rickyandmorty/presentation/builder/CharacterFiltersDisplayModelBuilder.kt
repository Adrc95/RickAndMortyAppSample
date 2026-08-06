package com.adrc95.rickyandmorty.presentation.builder

import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel

class CharacterFiltersDisplayModelBuilder {
    var species: FilterOptionDisplayModel.Species? = null
    var gender: FilterOptionDisplayModel.Gender? = null
    var status: FilterOptionDisplayModel.Status? = null

    fun withSpecies(species: FilterOptionDisplayModel.Species?) = apply { this.species = species }
    fun withGender(gender: FilterOptionDisplayModel.Gender?) = apply { this.gender = gender }
    fun withStatus(status: FilterOptionDisplayModel.Status?) = apply { this.status = status }

    fun build() = CharacterFiltersDisplayModel(
        species = species,
        gender = gender,
        status = status
    )
}

fun characterFiltersDisplayModel(
    block: CharacterFiltersDisplayModelBuilder.() -> Unit = {
    }
): CharacterFiltersDisplayModel = CharacterFiltersDisplayModelBuilder().apply(block).build()
