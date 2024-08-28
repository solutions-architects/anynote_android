package com.luckhost.domain.repository

interface MediaCacheRepoInterface {
    fun saveImageAndGetPath(uriString: String): String
    fun deleteImage(filePath: String)
    fun deleteListOfImages(filesPaths: List<String>)
}