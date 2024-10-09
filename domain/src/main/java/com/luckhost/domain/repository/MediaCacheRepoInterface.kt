package com.luckhost.domain.repository

/**
 * Repository that can save and delete media in the cache by uri
 *
 * Returns a new link (on a cached copy) on a media after it saved
 *
 * After the media, you need to save link to it in the "content" parameter of Note
 *
 * Before Note deleting you need to call the "deleteImage" or "deleteListOfImages"
 * method for each media specified in the "content" parameter of the note
 */
interface MediaCacheRepoInterface {
    fun saveImageAndGetPath(uriString: String): String
    fun deleteImage(filePath: String)
    fun deleteListOfImages(filesPaths: List<String>)
}