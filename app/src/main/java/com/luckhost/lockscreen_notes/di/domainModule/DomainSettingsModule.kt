package com.luckhost.lockscreen_notes.di.domainModule

import android.content.Context
import com.luckhost.data.localStorage.settings.NotesLayoutStorage
import com.luckhost.data.repository.NotesLayoutRepoImpl
import com.luckhost.domain.repository.NotesLayoutRepoInterface
import com.luckhost.domain.useCases.settings.GetColumnsCountUseCase
import com.luckhost.domain.useCases.settings.SetColumnsCountUseCase
import org.koin.dsl.module

val domainSettingsModule = module {
    factory<GetColumnsCountUseCase> {
        GetColumnsCountUseCase(
            repo = get<NotesLayoutRepoInterface>()
        )
    }

    factory<SetColumnsCountUseCase> {
        SetColumnsCountUseCase(
            repo = get<NotesLayoutRepoInterface>()
        )
    }

    single<NotesLayoutRepoInterface> {
        NotesLayoutRepoImpl(
            storage = get<NotesLayoutStorage>()
        )
    }

    single<NotesLayoutStorage> {
        NotesLayoutStorage(
            context = get<Context>()
        )
    }
}
