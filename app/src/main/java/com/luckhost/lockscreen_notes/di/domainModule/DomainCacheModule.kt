package com.luckhost.lockscreen_notes.di.domainModule

import com.luckhost.domain.useCases.cache.DeleteCachedImageUseCase
import com.luckhost.domain.useCases.cache.DeleteCachedImagesUseCase
import com.luckhost.domain.useCases.cache.GetCachedImageLinkUseCase
import org.koin.dsl.module

val domainCacheModule = module {
    factory<DeleteCachedImageUseCase> {
        DeleteCachedImageUseCase(mediaRepo = get())
    }

    factory<DeleteCachedImagesUseCase> {
        DeleteCachedImagesUseCase(mediaRepo = get())
    }

    factory<GetCachedImageLinkUseCase> {
        GetCachedImageLinkUseCase(mediaRepo = get())
    }
}