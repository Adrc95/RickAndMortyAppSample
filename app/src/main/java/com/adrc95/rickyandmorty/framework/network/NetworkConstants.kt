package com.adrc95.rickyandmorty.framework.network

object NetworkConstants {
    const val CONNECT_TIMEOUT_SECONDS = 10L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L

    const val CACHE_SIZE = 10L * 1024 * 1024 // 10 MB

    const val CACHE_DIR = "http_cache"

    const val IMAGE_CACHE_DIR = "image_cache"

    const val IMAGE_CACHE_SIZE_BYTES = 50L * 1024 * 1024 // 50 MB

    const val IMAGE_MEMORY_CACHE_PERCENT = 0.25

    const val JSON_TYPE = "application/json"
}
