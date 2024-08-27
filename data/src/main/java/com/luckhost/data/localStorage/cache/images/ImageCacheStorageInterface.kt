package com.luckhost.data.localStorage.cache.images

import android.net.Uri

interface ImageCacheStorageInterface {
    fun saveIfNotExistAndGetImageLink(uri: Uri): String
    fun deleteCachedImages(uri: List<Uri>)
}