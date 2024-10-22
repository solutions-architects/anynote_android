package com.luckhost.lockscreen_notes.di
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteViewModel
import com.luckhost.lockscreen_notes.presentation.main.MainViewModel
import com.luckhost.lockscreen_notes.presentation.userLogin.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { ResourceProvider(get()) }

    viewModel<MainViewModel>{
        MainViewModel(
            getNotesUseCase = get(),
            deleteNoteUseCase = get(),
            getLocalAuthTokenUseCase = get(),
            getFilteredMdUseCase = get(),
            deleteCachedImagesUseCase = get(),
            saveLocalAuthTokenUseCase = get(),
            resourceProvider = get()
        )
    }
    viewModel<OpenNoteViewModel>{
        OpenNoteViewModel(
            saveNoteUseCase = get(),
            getNoteByHashUseCase = get(),
            changeNoteUseCase = get(),
            getCachedImageLinkUseCase = get(),
            deleteCachedImageUseCase = get(),
            resourceProvider = get(),
        )
    }
    viewModel<LoginViewModel>{
        LoginViewModel(
            getAuthTokenUseCase = get(),
            signUpUseCase = get(),
            resourceProvider = get()
        )
    }
}