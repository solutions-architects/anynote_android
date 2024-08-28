package com.luckhost.lockscreen_notes.app

import android.app.Application
import com.luckhost.lockscreen_notes.di.appModule
import com.luckhost.lockscreen_notes.di.dataModule
import com.luckhost.lockscreen_notes.di.domainModule.domainCacheModule
import com.luckhost.lockscreen_notes.di.domainModule.domainHashRepoModule
import com.luckhost.lockscreen_notes.di.domainModule.domainModule
import com.luckhost.lockscreen_notes.di.domainModule.domainNetworkModule
import com.luckhost.lockscreen_notes.di.domainModule.domainNoteRepoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule,
                domainCacheModule, domainNetworkModule,
                domainHashRepoModule, domainNoteRepoModule
                )
            )
        }
    }
}