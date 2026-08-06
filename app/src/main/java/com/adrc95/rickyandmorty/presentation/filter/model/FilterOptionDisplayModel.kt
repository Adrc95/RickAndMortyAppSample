package com.adrc95.rickyandmorty.presentation.filter.model

import androidx.annotation.StringRes
import com.adrc95.rickyandmorty.R

sealed class FilterOptionDisplayModel(@StringRes open val label: Int) {

    sealed class Species(
        @StringRes override val label: Int
    ) : FilterOptionDisplayModel(R.string.species) {

        data object Human : Species(R.string.filter_species_human)

        data object Alien : Species(R.string.filter_species_alien)

        data object Robot : Species(R.string.filter_species_robot)

        data object Animal : Species(R.string.filter_species_animal)

        data object Disease : Species(R.string.filter_species_disease)

        data object Cronenberg : Species(R.string.filter_species_cronenberg)

        data object Poopybutthole : Species(R.string.filter_species_poopybutthole)

        data object MythologicalCreature : Species(R.string.filter_species_mythological_creature)

        data object Unknown : Species(R.string.filter_species_unknown)
    }

    sealed class Gender(
        @StringRes override val label: Int
    ) : FilterOptionDisplayModel(R.string.genders) {

        data object Female : Gender(R.string.filter_gender_female)

        data object Male : Gender(R.string.filter_gender_male)

        data object Genderless : Gender(R.string.filter_gender_genderless)

        data object Unknown : Gender(R.string.filter_gender_unknown)
    }

    sealed class Status(
        @StringRes override val label: Int
    ) : FilterOptionDisplayModel(R.string.status) {

        data object Alive : Status(R.string.filter_status_alive)

        data object Dead : Status(R.string.filter_status_dead)

        data object Unknown : Status(R.string.filter_status_unknown)
    }
}