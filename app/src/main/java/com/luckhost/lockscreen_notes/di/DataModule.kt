package com.luckhost.lockscreen_notes.di

import com.luckhost.data.repository.NoteHashesRepoImpl
import com.luckhost.data.repository.NotesRepositoryImpl
import com.luckhost.data.localStorage.keys.HashStorage
import com.luckhost.data.localStorage.keys.SharedPrefHashesStorage
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.data.localStorage.materials.SharedPrefNotesStorage
import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.retrofit.RetrofitModule
import com.luckhost.data.repository.NetworkServiceImpl
import com.luckhost.domain.repository.NetworkServiceInterface
import com.luckhost.domain.repository.NoteHashesRepoInterface
import com.luckhost.domain.repository.NotesRepositoryInterface
import org.koin.dsl.module

val dataModule = module {
    single<NotesStorage> {
        SharedPrefNotesStorage(context = get())
    }

    single<NotesRepositoryInterface> {
        NotesRepositoryImpl(notesStorage = get())
    }

    single<HashStorage> {
        SharedPrefHashesStorage(context = get())
    }

    single<NoteHashesRepoInterface> {
        NoteHashesRepoImpl(hashStorage = get())
    }

    single<NetworkModule> {
        RetrofitModule()
    }

    single<NetworkServiceInterface> {
        NetworkServiceImpl(networkModule = get())
    }
}