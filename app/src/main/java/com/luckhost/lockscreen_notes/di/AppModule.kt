package com.luckhost.lockscreen_notes.di
import com.luckhost.lockscreen_notes.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel<MainViewModel>{
            MainViewModel(/*past usecases here*/)
    }
}