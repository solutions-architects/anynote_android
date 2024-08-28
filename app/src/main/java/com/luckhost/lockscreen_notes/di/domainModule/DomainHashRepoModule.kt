package com.luckhost.lockscreen_notes.di.domainModule

import com.luckhost.domain.useCases.keys.AddHashUseCase
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import org.koin.dsl.module

val domainHashRepoModule = module {
    factory<GetHashesUseCase> {
        GetHashesUseCase(
            hashesRepo = get(),
        )
    }
    factory<SaveHashesUseCase> {
        SaveHashesUseCase(
            hashesRepository = get(),
        )
    }
    factory<DeleteHashUseCase> {
        DeleteHashUseCase(
            hashesRepository = get(),
        )
    }
    factory<AddHashUseCase> {
        AddHashUseCase(
            hashesRepository = get(),
        )
    }
}