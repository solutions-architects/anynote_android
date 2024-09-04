package com.luckhost.lockscreen_notes.di.domainModule

import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.objects.GetNoteByHashUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import org.koin.dsl.module

val domainNoteRepoModule = module{
    factory<GetNotesUseCase> {
        GetNotesUseCase(
            repository = get(),
        )
    }
    factory<SaveNoteUseCase> {
        SaveNoteUseCase(
            repository = get(),
        )
    }
    factory<DeleteNoteUseCase> {
        DeleteNoteUseCase(
            repository = get(),
        )
    }
    factory<ChangeNoteUseCase> {
        ChangeNoteUseCase(
            repository = get(),
        )
    }
    factory<GetNoteByHashUseCase> {
        GetNoteByHashUseCase(
            repository = get()
        )
    }
}