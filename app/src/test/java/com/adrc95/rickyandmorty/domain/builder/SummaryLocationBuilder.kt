package com.adrc95.rickyandmorty.domain.builder

import com.adrc95.rickyandmorty.domain.model.SummaryLocation

class SummaryLocationBuilder {
    var id: Int = 1
    var name: String = "Earth (C-137)"

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }

    fun build() = SummaryLocation(
        id = id,
        name = name
    )
}

fun summaryLocation(block: SummaryLocationBuilder.() -> Unit = {}): SummaryLocation =
    SummaryLocationBuilder().apply(block).build()
