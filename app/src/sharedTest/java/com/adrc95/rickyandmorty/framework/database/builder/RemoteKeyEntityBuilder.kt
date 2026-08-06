package com.adrc95.rickyandmorty.framework.database.builder

import com.adrc95.rickyandmorty.framework.database.entity.RemoteKeyEntity

class RemoteKeyEntityBuilder {
    var resource: String = "characters"
    var nextPage: Int? = 2
    var lastUpdatedAt: Long = System.currentTimeMillis()

    fun withResource(resource: String) = apply { this.resource = resource }
    fun withNextPage(nextPage: Int?) = apply { this.nextPage = nextPage }
    fun withLastUpdatedAt(lastUpdatedAt: Long) = apply { this.lastUpdatedAt = lastUpdatedAt }

    fun build() = RemoteKeyEntity(
        resource = resource,
        nextPage = nextPage,
        lastUpdatedAt = lastUpdatedAt,
    )
}

fun remoteKeyEntity(block: RemoteKeyEntityBuilder.() -> Unit = {}): RemoteKeyEntity =
    RemoteKeyEntityBuilder().apply(block).build()
