package com.luckhost.lockscreen_notes.di
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import com.luckhost.lockscreen_notes.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel<MainViewModel>{
            MainViewModel(
                getNotesUseCase = get(),
                saveNoteUseCase = get(),
                deleteNoteUseCase = get(),
                getHashesUseCase = get(),
                saveHashesUseCase = get(),
                deleteHashUseCase = get(),)
    }
}