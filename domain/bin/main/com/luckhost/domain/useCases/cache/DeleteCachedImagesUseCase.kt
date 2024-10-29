package com.luckhost.domain.useCases.cache

import com.luckhost.domain.repository.MediaCacheRepoInterface

class DeleteCachedImagesUseCase(
    private val mediaRepo: MediaCacheRepoInterface
) {
    fun execute(filesPaths: List<String>) {
        mediaRepo.deleteListOfImages(filesPaths = filesPaths)
    }
}