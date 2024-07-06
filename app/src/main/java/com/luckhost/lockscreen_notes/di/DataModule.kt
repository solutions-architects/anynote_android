package com.luckhost.lockscreen_notes.di

import com.luckhost.data.repository.NoteHashesRepoImpl
import com.luckhost.data.repository.NotesRepositoryImpl
import com.luckhost.data.storage.keys.HashStorage
import com.luckhost.data.storage.keys.SharedPrefHashesStorage
import com.luckhost.data.storage.materials.NotesStorage
import com.luckhost.data.storage.materials.SharedPrefNotesStorage
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
}