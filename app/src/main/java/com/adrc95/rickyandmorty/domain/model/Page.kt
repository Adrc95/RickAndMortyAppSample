package com.adrc95.rickyandmorty.domain.model

data class Page<T>(
    val data: List<T>,
    val nextPage: Int?
)