package com.adrc95.rickyandmorty.framework.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_CACHE_SIZE_BYTES
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_MEMORY_CACHE_PERCENT
import okio.Path

fun createImageLoader(
    context: PlatformContext,
    diskCacheDirectory: Path,
): ImageLoader = ImageLoader.Builder(context)
    .memoryCache {
        MemoryCache.Builder()
            .maxSizePercent(context, IMAGE_MEMORY_CACHE_PERCENT)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(diskCacheDirectory)
            .maxSizeBytes(IMAGE_CACHE_SIZE_BYTES)
            .build()
    }
    .build()
