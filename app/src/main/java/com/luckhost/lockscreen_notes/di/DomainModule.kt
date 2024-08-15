package com.luckhost.lockscreen_notes.di

import com.luckhost.domain.useCases.keys.AddHashUseCase
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import com.luckhost.domain.useCases.network.ChangeNoteOnServer
import com.luckhost.domain.useCases.network.ChangeUserAccountParamsUseCase
import com.luckhost.domain.useCases.network.CreateNoteOnServerUseCase
import com.luckhost.domain.useCases.network.GetAllNotesFromServerUseCase
import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.network.GetLocalAuthTokenUseCase
import com.luckhost.domain.useCases.network.GetUserAccountParamsUseCase
import com.luckhost.domain.useCases.network.RefreshAccTokenUseCase
import com.luckhost.domain.useCases.network.SignUpUseCase
import com.luckhost.domain.useCases.network.VerifyTokenUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import org.koin.dsl.module

val domainModule = module {
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

    factory<ChangeNoteOnServer> {
        ChangeNoteOnServer(
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
//    factory<GetLocalAuthTokenUseCase> {
//        GetLocalAuthTokenUseCase(
//            repository = get(),
//        )
//    }
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