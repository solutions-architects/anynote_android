package com.luckhost.lockscreen_notes.di

import com.luckhost.data.localStorage.cache.images.ImageCacheStorageImpl
import com.luckhost.data.localStorage.cache.images.ImageCacheStorageInterface
import com.luckhost.data.repository.NotesRepositoryImpl
import com.luckhost.data.localStorage.keys.tokens.SharedPrefTokensStorage
import com.luckhost.data.localStorage.keys.tokens.TokensStorage
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.data.localStorage.materials.sqlite.SQLiteNotesStorage
import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.retrofit.RetrofitModule
import com.luckhost.data.repository.AuthTokensRepoImpl
import com.luckhost.data.repository.MediaCacheRepoImpl
import com.luckhost.data.repository.NetworkServiceImpl
import com.luckhost.domain.repository.AuthTokensRepoInterface
import com.luckhost.domain.repository.MediaCacheRepoInterface
import com.luckhost.domain.repository.NetworkServiceInterface
import com.luckhost.domain.repository.NotesRepositoryInterface
import org.koin.dsl.module

val dataModule = module {
    single<NotesStorage> {
        SQLiteNotesStorage(context = get())
    }

    single<NotesRepositoryInterface> {
        NotesRepositoryImpl(notesStorage = get())
    }

    single<NetworkModule> {
        RetrofitModule()
    }

    single<NetworkServiceInterface> {
        NetworkServiceImpl(networkModule = get())
    }

    single<AuthTokensRepoInterface> {
        AuthTokensRepoImpl(storage = get())
    }

    single<TokensStorage> {
        SharedPrefTokensStorage(context = get())
    }

    single<MediaCacheRepoInterface> {
        MediaCacheRepoImpl(
            context = get(),
            imageCacheStorage = get())
    }

    single<ImageCacheStorageInterface> {
        ImageCacheStorageImpl(context = get())
    }
}