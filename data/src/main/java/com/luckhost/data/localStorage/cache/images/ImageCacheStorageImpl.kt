package com.luckhost.data.localStorage.cache.images

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ImageCacheStorageImpl(
    val context: Context
): ImageCacheStorageInterface {
    override fun saveIfNotExistAndGetImageLink(uri: Uri, fileName: String): String {
        val file = File(context.cacheDir, fileName)
        val contentResolver = context.contentResolver

        if (file.exists()) {
            return file.absolutePath
        }

        contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.absolutePath
    }

    override fun deleteImage(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        } else {
            throw NoSuchFileException(
                file = file,
                reason = "No file with \"$filePath\" path in cache storage"
            )
        }
    }

    override fun deleteCachedImages(filesPaths: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            filesPaths.forEach {
                deleteImage(it)
            }
        }
    }
}