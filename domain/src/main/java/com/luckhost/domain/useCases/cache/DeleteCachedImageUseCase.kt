package com.luckhost.domain.useCases.cache

import com.luckhost.domain.repository.MediaCacheRepoInterface

class DeleteCachedImageUseCase(
    private val mediaRepo: MediaCacheRepoInterface
) {
    fun execute(filePath: String) {
        mediaRepo.deleteImage(filePath = filePath)
    }
}