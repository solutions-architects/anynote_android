package com.luckhost.data.localStorage.cache.images

import android.net.Uri

/**
 * The part of media cache repo
 *
 * Works only with images
 */
interface ImageCacheStorageInterface {
    fun saveIfNotExistAndGetImageLink(uri: Uri, fileName: String): String
    fun deleteImage(filePath: String)
    fun deleteCachedImages(filesPaths: List<String>)
}