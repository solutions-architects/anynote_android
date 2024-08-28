package com.luckhost.lockscreen_notes.di.domainModule

import com.luckhost.domain.useCases.network.ChangeNoteOnServerUseCase
import com.luckhost.domain.useCases.network.ChangeUserAccountParamsUseCase
import com.luckhost.domain.useCases.network.CreateNoteOnServerUseCase
import com.luckhost.domain.useCases.network.GetAllNotesFromServerUseCase
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.network.GetUserAccountParamsUseCase
import com.luckhost.domain.useCases.network.RefreshAccTokenUseCase
import com.luckhost.domain.useCases.network.SignUpUseCase
import com.luckhost.domain.useCases.network.VerifyTokenUseCase
import com.luckhost.domain.useCases.network.localActions.GetLocalAuthTokenUseCase
import com.luckhost.domain.useCases.network.localActions.SaveLocalAuthTokenUseCase
import org.koin.dsl.module

val domainNetworkModule = module {
    factory<ChangeNoteOnServerUseCase> {
        ChangeNoteOnServerUseCase(
            netApi = get(),
        )
    }
    factory<ChangeUserAccountParamsUseCase> {
        ChangeUserAccountParamsUseCase(
            netApi = get(),
        )
    }
    factory<CreateNoteOnServerUseCase> {
        CreateNoteOnServerUseCase(
            netApi = get(),
        )
    }
    factory<GetAllNotesFromServerUseCase> {
        GetAllNotesFromServerUseCase(
            netApi = get(),
        )
    }
    factory<GetAuthTokenUseCase> {
        GetAuthTokenUseCase(
            netApi = get(),
        )
    }
    factory<GetLocalAuthTokenUseCase> {
        GetLocalAuthTokenUseCase(
            repository = get(),
        )
    }
    factory<SaveLocalAuthTokenUseCase> {
        SaveLocalAuthTokenUseCase(
            repository = get(),
        )
    }
    factory<GetUserAccountParamsUseCase> {
        GetUserAccountParamsUseCase(
            netApi = get(),
        )
    }
    factory<RefreshAccTokenUseCase> {
        RefreshAccTokenUseCase(
            netApi = get(),
        )
    }
    factory<SignUpUseCase> {
        SignUpUseCase(
            netApi = get(),
        )
    }
    factory<VerifyTokenUseCase> {
        VerifyTokenUseCase(
            netApi = get(),
        )
    }
}