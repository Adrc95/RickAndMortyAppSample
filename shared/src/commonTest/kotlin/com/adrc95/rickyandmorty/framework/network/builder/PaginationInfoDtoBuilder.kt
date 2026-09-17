package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.PaginationInfoDto

class PaginationInfoDtoBuilder {
    var count: Int = 826
    var pages: Int = 42
    var next: String? = "https://rickandmortyapi.com/api/character?page=2"
    var prev: String? = null

    fun withCount(count: Int) = apply { this.count = count }
    fun withPages(pages: Int) = apply { this.pages = pages }
    fun withNext(next: String?) = apply { this.next = next }
    fun withPrev(prev: String?) = apply { this.prev = prev }

    fun build() = PaginationInfoDto(
        count = count,
        pages = pages,
        next = next,
        prev = prev
    )
}

fun paginationInfoDto(block: PaginationInfoDtoBuilder.() -> Unit = {}): PaginationInfoDto =
    PaginationInfoDtoBuilder().apply(block).build()
