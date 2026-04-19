package com.luckhost.lockscreen_notes.di.domainModule

import android.content.Context
import com.luckhost.data.localStorage.settings.UiThemeStorage
import com.luckhost.data.repository.UiThemeRepoImpl
import com.luckhost.domain.repository.UiThemeRepoInterface
import com.luckhost.domain.useCases.theme.GetThemeStateUseCase
import com.luckhost.domain.useCases.theme.ToggleThemeUseCase
import org.koin.dsl.module

val domainThemeModule = module {
    factory<GetThemeStateUseCase> {
        GetThemeStateUseCase(
            repo = get<UiThemeRepoInterface>()
        )
    }
    
    factory<ToggleThemeUseCase> {
        ToggleThemeUseCase(
            repo = get<UiThemeRepoInterface>()
        )
    }

    single<UiThemeRepoInterface> {
        UiThemeRepoImpl(
            storage = get<UiThemeStorage>()
        )
    }

    single<UiThemeStorage> {
        UiThemeStorage(
            context = get<Context>()
        )
    }
}