package com.adrc95.rickyandmorty.presentation.filter.mapper

import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_FEMALE
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_FEMALE_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GENDERLESS
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GENDERLESS_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_MALE
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_MALE_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ALIEN
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ALIEN_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ANIMAL
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ANIMAL_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_CRONENBERG
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_CRONENBERG_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_DISEASE
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_DISEASE_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_HUMAN
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_HUMAN_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_MYTHOLOGICAL
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_MYTHOLOGICAL_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_POOPYBUTTHOLE
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_POOPYBUTTHOLE_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ROBOT
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ROBOT_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_ALIVE
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_ALIVE_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_DEAD
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_DEAD_QUERY
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.UNKNOWN
import com.adrc95.rickyandmorty.domain.model.FilterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption
import com.adrc95.rickyandmorty.presentation.filter.model.FilterGroupDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel

fun FilterGroup.toDisplayModel(): FilterGroupDisplayModel = FilterGroupDisplayModel(
    id = id,
    title = when (id) {
        SPECIES_GROUP_ID -> R.string.species
        GENDER_GROUP_ID -> R.string.genders
        STATUS_GROUP_ID -> R.string.status
        else -> R.string.species
    },
    options = options.map { it.toDisplayModel(groupId = id) }
)

private fun FilterOption.toDisplayModel(groupId: String): FilterOptionDisplayModel = when (groupId) {
    GENDER_GROUP_ID -> when (id) {
        GENDER_FEMALE -> FilterOptionDisplayModel.Gender.Female
        GENDER_MALE -> FilterOptionDisplayModel.Gender.Male
        GENDER_GENDERLESS -> FilterOptionDisplayModel.Gender.Genderless
        else -> FilterOptionDisplayModel.Gender.Unknown
    }

    STATUS_GROUP_ID -> when (id) {
        STATUS_ALIVE -> FilterOptionDisplayModel.Status.Alive
        STATUS_DEAD -> FilterOptionDisplayModel.Status.Dead
        else -> FilterOptionDisplayModel.Status.Unknown
    }

    else -> when (id) {
        SPECIES_HUMAN -> FilterOptionDisplayModel.Species.Human
        SPECIES_ALIEN -> FilterOptionDisplayModel.Species.Alien
        SPECIES_ROBOT -> FilterOptionDisplayModel.Species.Robot
        SPECIES_ANIMAL -> FilterOptionDisplayModel.Species.Animal
        SPECIES_DISEASE -> FilterOptionDisplayModel.Species.Disease
        SPECIES_CRONENBERG -> FilterOptionDisplayModel.Species.Cronenberg
        SPECIES_POOPYBUTTHOLE -> FilterOptionDisplayModel.Species.Poopybutthole
        SPECIES_MYTHOLOGICAL -> FilterOptionDisplayModel.Species.MythologicalCreature
        else -> FilterOptionDisplayModel.Species.Unknown
    }
}

fun FilterOptionDisplayModel.toDomain(): String = when (this) {
    FilterOptionDisplayModel.Species.Human -> SPECIES_HUMAN_QUERY
    FilterOptionDisplayModel.Species.Alien -> SPECIES_ALIEN_QUERY
    FilterOptionDisplayModel.Species.Robot -> SPECIES_ROBOT_QUERY
    FilterOptionDisplayModel.Species.Animal -> SPECIES_ANIMAL_QUERY
    FilterOptionDisplayModel.Species.Disease -> SPECIES_DISEASE_QUERY
    FilterOptionDisplayModel.Species.Cronenberg -> SPECIES_CRONENBERG_QUERY
    FilterOptionDisplayModel.Species.Poopybutthole -> SPECIES_POOPYBUTTHOLE_QUERY
    FilterOptionDisplayModel.Species.MythologicalCreature -> SPECIES_MYTHOLOGICAL_QUERY
    FilterOptionDisplayModel.Species.Unknown -> UNKNOWN
    FilterOptionDisplayModel.Gender.Female -> GENDER_FEMALE_QUERY
    FilterOptionDisplayModel.Gender.Male -> GENDER_MALE_QUERY
    FilterOptionDisplayModel.Gender.Genderless -> GENDER_GENDERLESS_QUERY
    FilterOptionDisplayModel.Gender.Unknown -> UNKNOWN
    FilterOptionDisplayModel.Status.Alive -> STATUS_ALIVE_QUERY
    FilterOptionDisplayModel.Status.Dead -> STATUS_DEAD_QUERY
    FilterOptionDisplayModel.Status.Unknown -> UNKNOWN
}
