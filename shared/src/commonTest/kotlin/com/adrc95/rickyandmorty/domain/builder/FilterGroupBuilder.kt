package com.adrc95.rickyandmorty.domain.builder

import com.adrc95.rickyandmorty.domain.model.FilterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption

class FilterGroupBuilder {
    var id: String = "species"
    var options: List<FilterOption> = listOf(
        FilterOption(id = "human"),
        FilterOption(id = "alien")
    )

    fun withId(id: String) = apply { this.id = id }
    fun withOptions(options: List<FilterOption>) = apply { this.options = options }

    fun build() = FilterGroup(
        id = id,
        options = options
    )
}

fun filterGroup(block: FilterGroupBuilder.() -> Unit = {}): FilterGroup = FilterGroupBuilder().apply(block).build()
