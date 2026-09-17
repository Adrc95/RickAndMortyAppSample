package com.adrc95.rickyandmorty.framework.network.cache

import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CACHE_SIZE
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.NEW_LINE
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import io.ktor.util.flattenEntries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.ByteString.Companion.encodeUtf8
import okio.FileSystem
import okio.IOException
import okio.Path
import okio.SYSTEM
import okio.buffer
import okio.use

class OkioCacheStorage(
    private val directory: Path,
    private val fileSystem: FileSystem = FileSystem.SYSTEM,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : CacheStorage {

    private val mutex = Mutex()

    init {
        try {
            fileSystem.createDirectories(directory)
        } catch (e: IOException) {
            Napier.w("OkioCacheStorage: failed to create directory $directory", e)
        }
    }

    override suspend fun store(url: Url, data: CachedResponseData): Unit = withContext(dispatcher) {
        mutex.withLock {
            val key = key(url)
            val updated = read(key).filterNot { it.varyKeys == data.varyKeys } + data
            write(key, updated)
        }
    }

    override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? =
        withContext(dispatcher) {
            mutex.withLock {
                read(key(url)).find { cached ->
                    varyKeys.all { (name, value) -> cached.varyKeys[name] == value }
                }
            }
        }

    override suspend fun findAll(url: Url): Set<CachedResponseData> = withContext(dispatcher) {
        mutex.withLock { read(key(url)).toSet() }
    }

    override suspend fun remove(url: Url, varyKeys: Map<String, String>): Unit = withContext(dispatcher) {
        mutex.withLock {
            val key = key(url)
            write(key, read(key).filterNot { it.varyKeys == varyKeys })
        }
    }

    override suspend fun removeAll(url: Url): Unit = withContext(dispatcher) {
        mutex.withLock {
            fileSystem.delete(path(key(url)), mustExist = false)
        }
    }

    private fun key(url: Url): String = url.toString().encodeUtf8().sha256().hex()

    private fun path(key: String): Path = directory / key

    private fun read(key: String): List<CachedResponseData> {
        val file = path(key)
        if (!fileSystem.exists(file)) return emptyList()
        return try {
            fileSystem.source(file).buffer().use { source ->
                List(source.readInt()) { readEntry(source) }
            }
        } catch (e: IOException) {
            Napier.w("OkioCacheStorage: failed to read cache file $file", e)
            emptyList()
        }
    }

    private fun write(key: String, entries: List<CachedResponseData>) {
        fileSystem.sink(path(key)).buffer().use { sink ->
            sink.writeInt(entries.size)
            entries.forEach { writeEntry(sink, it) }
        }
        evictIfNeeded()
    }

    private fun evictIfNeeded() {
        val files = try {
            fileSystem.list(directory)
        } catch (e: IOException) {
            Napier.w("OkioCacheStorage: failed to list cache directory $directory", e)
            return
        }

        var totalSize = 0L
        val entries = files.mapNotNull { path ->
            val metadata = fileSystem.metadataOrNull(path) ?: return@mapNotNull null
            val size = metadata.size ?: 0L
            totalSize += size
            path to (metadata.lastModifiedAtMillis ?: 0L)
        }
        if (totalSize <= CACHE_SIZE) return

        entries.sortedBy { (_, lastModifiedAtMillis) -> lastModifiedAtMillis }.forEach { (path, _) ->
            if (totalSize <= CACHE_SIZE) return
            val size = fileSystem.metadataOrNull(path)?.size ?: 0L
            try {
                fileSystem.delete(path, mustExist = false)
                totalSize -= size
            } catch (e: IOException) {
                Napier.w("OkioCacheStorage: failed to evict cache file $path", e)
            }
        }
    }

    private fun writeEntry(sink: BufferedSink, data: CachedResponseData) {
        sink.writeUtf8(data.url.toString() + NEW_LINE)
        sink.writeInt(data.statusCode.value)
        sink.writeUtf8(data.statusCode.description + NEW_LINE)
        sink.writeUtf8(data.version.toString() + NEW_LINE)
        val headers = data.headers.flattenEntries()
        sink.writeInt(headers.size)
        headers.forEach { (name, value) ->
            sink.writeUtf8(name + NEW_LINE)
            sink.writeUtf8(value + NEW_LINE)
        }
        sink.writeLong(data.requestTime.timestamp)
        sink.writeLong(data.responseTime.timestamp)
        sink.writeLong(data.expires.timestamp)
        sink.writeInt(data.varyKeys.size)
        data.varyKeys.forEach { (name, value) ->
            sink.writeUtf8(name.lowercase() + NEW_LINE)
            sink.writeUtf8(value + NEW_LINE)
        }
        sink.writeInt(data.body.size)
        sink.write(data.body)
    }

    private fun readEntry(source: BufferedSource): CachedResponseData {
        val url = source.readUtf8LineStrict()
        val status = HttpStatusCode(source.readInt(), source.readUtf8LineStrict())
        val version = HttpProtocolVersion.parse(source.readUtf8LineStrict())
        val headers = HeadersBuilder().apply {
            repeat(source.readInt()) {
                append(source.readUtf8LineStrict(), source.readUtf8LineStrict())
            }
        }.build()
        val requestTime = GMTDate(source.readLong())
        val responseTime = GMTDate(source.readLong())
        val expires = GMTDate(source.readLong())
        val varyKeys = buildMap {
            repeat(source.readInt()) {
                put(source.readUtf8LineStrict().lowercase(), source.readUtf8LineStrict())
            }
        }
        val body = source.readByteArray(source.readInt().toLong())
        return CachedResponseData(
            url = Url(url),
            statusCode = status,
            requestTime = requestTime,
            responseTime = responseTime,
            version = version,
            expires = expires,
            headers = headers,
            varyKeys = varyKeys,
            body = body,
        )
    }
}
