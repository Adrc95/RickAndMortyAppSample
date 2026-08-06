package com.adrc95.rickyandmorty.presentation.filter.mapper

import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.model.FilterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption
import com.adrc95.rickyandmorty.domain.usecase.GetFilterGroupsUseCase
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.model.FilterGroupDisplayModel

fun FilterGroup.toDisplayModel(): FilterGroupDisplayModel = FilterGroupDisplayModel(
    id = id,
    title = when (id) {
        GetFilterGroupsUseCase.SPECIES_GROUP_ID -> R.string.species
        GetFilterGroupsUseCase.GENDER_GROUP_ID -> R.string.genders
        GetFilterGroupsUseCase.STATUS_GROUP_ID -> R.string.status
        else -> R.string.species
    },
    options = options.map { it.toDisplayModel(groupId = id) },
)

private fun FilterOption.toDisplayModel(groupId: String): FilterOptionDisplayModel =
    when (groupId) {
        GetFilterGroupsUseCase.GENDER_GROUP_ID -> when (id) {
            "female" -> FilterOptionDisplayModel.Gender.Female
            "male" -> FilterOptionDisplayModel.Gender.Male
            "genderless" -> FilterOptionDisplayModel.Gender.Genderless
            else -> FilterOptionDisplayModel.Gender.Unknown
        }
        GetFilterGroupsUseCase.STATUS_GROUP_ID -> when (id) {
            "alive" -> FilterOptionDisplayModel.Status.Alive
            "dead" -> FilterOptionDisplayModel.Status.Dead
            else -> FilterOptionDisplayModel.Status.Unknown
        }
        else -> when (id) {
            "human" -> FilterOptionDisplayModel.Species.Human
            "alien" -> FilterOptionDisplayModel.Species.Alien
            "robot" -> FilterOptionDisplayModel.Species.Robot
            "animal" -> FilterOptionDisplayModel.Species.Animal
            "disease" -> FilterOptionDisplayModel.Species.Disease
            "cronenberg" -> FilterOptionDisplayModel.Species.Cronenberg
            "poopybutthole" -> FilterOptionDisplayModel.Species.Poopybutthole
            "mythological" -> FilterOptionDisplayModel.Species.MythologicalCreature
            else -> FilterOptionDisplayModel.Species.Unknown
        }
    }

fun FilterOptionDisplayModel.toDomain(): String = when (this) {
    FilterOptionDisplayModel.Species.Human -> "Human"
    FilterOptionDisplayModel.Species.Alien -> "Alien"
    FilterOptionDisplayModel.Species.Robot -> "Robot"
    FilterOptionDisplayModel.Species.Animal -> "Animal"
    FilterOptionDisplayModel.Species.Disease -> "Disease"
    FilterOptionDisplayModel.Species.Cronenberg -> "Cronenberg"
    FilterOptionDisplayModel.Species.Poopybutthole -> "Poopybutthole"
    FilterOptionDisplayModel.Species.MythologicalCreature -> "Mythological Creature"
    FilterOptionDisplayModel.Species.Unknown -> "unknown"
    FilterOptionDisplayModel.Gender.Female -> "Female"
    FilterOptionDisplayModel.Gender.Male -> "Male"
    FilterOptionDisplayModel.Gender.Genderless -> "Genderless"
    FilterOptionDisplayModel.Gender.Unknown -> "unknown"
    FilterOptionDisplayModel.Status.Alive -> "Alive"
    FilterOptionDisplayModel.Status.Dead -> "Dead"
    FilterOptionDisplayModel.Status.Unknown -> "unknown"
}
