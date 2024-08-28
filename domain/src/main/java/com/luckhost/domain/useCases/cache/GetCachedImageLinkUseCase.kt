package com.luckhost.domain.useCases.cache

import com.luckhost.domain.repository.MediaCacheRepoInterface

class GetCachedImageLinkUseCase(
    private val mediaRepo: MediaCacheRepoInterface
) {
    fun execute(uriString: String): String {
        return mediaRepo.saveImageAndGetPath(uriString)
    }
}