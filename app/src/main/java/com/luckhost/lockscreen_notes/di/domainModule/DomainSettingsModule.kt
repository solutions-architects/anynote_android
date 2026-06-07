package com.luckhost.lockscreen_notes.di.domainModule

import android.content.Context
import com.luckhost.data.localStorage.settings.LanguageStorage
import com.luckhost.data.localStorage.settings.NotesLayoutStorage
import com.luckhost.data.repository.LanguageRepoImpl
import com.luckhost.data.repository.NotesLayoutRepoImpl
import com.luckhost.domain.repository.LanguageRepoInterface
import com.luckhost.domain.repository.NotesLayoutRepoInterface
import com.luckhost.domain.useCases.settings.GetColumnsCountUseCase
import com.luckhost.domain.useCases.settings.GetLanguageUseCase
import com.luckhost.domain.useCases.settings.SetColumnsCountUseCase
import com.luckhost.domain.useCases.settings.SetLanguageUseCase
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

    factory<GetLanguageUseCase> {
        GetLanguageUseCase(repo = get<LanguageRepoInterface>())
    }

    factory<SetLanguageUseCase> {
        SetLanguageUseCase(repo = get<LanguageRepoInterface>())
    }

    single<LanguageRepoInterface> {
        LanguageRepoImpl(storage = get<LanguageStorage>())
    }

    single<LanguageStorage> {
        LanguageStorage(context = get<Context>())
    }
}
