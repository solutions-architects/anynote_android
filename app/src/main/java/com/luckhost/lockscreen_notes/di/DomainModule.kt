package com.luckhost.lockscreen_notes.di

import com.luckhost.domain.useCases.GetNoteUseCase
import com.luckhost.domain.useCases.SaveNoteUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetNoteUseCase(get(), get())
        SaveNoteUseCase(get())
    }
}