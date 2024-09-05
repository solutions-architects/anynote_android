package com.luckhost.lockscreen_notes.di.domainModule

import com.luckhost.domain.useCases.filters.GetFilteredMdUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<GetFilteredMdUseCase> {
        GetFilteredMdUseCase()
    }
}