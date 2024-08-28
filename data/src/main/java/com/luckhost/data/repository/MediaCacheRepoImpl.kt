package com.luckhost.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.luckhost.data.localStorage.cache.images.ImageCacheStorageInterface
import com.luckhost.domain.repository.MediaCacheRepoInterface

class MediaCacheRepoImpl(
    private val context: Context,
    private val imageCacheStorage: ImageCacheStorageInterface
): MediaCacheRepoInterface {
    override fun saveImageAndGetPath(uriString: String): String {
        return imageCacheStorage.saveIfNotExistAndGetImageLink(
            uri = Uri.parse(uriString),
            fileName = getFileName(uriString)
        )
    }

    override fun deleteImage(filePath: String) {
        imageCacheStorage.deleteImage(filePath)
    }

    override fun deleteListOfImages(filesPaths: List<String>) {
        imageCacheStorage.deleteCachedImages(filesPaths)
    }

    private fun getFileName(uriString: String): String {
        val uri = Uri.parse(uriString)
        var name = ""
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }
}