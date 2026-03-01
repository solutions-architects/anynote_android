package com.luckhost.lockscreen_notes.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.luckhost.data.localStorage.theme.UiThemeStorage
import com.luckhost.lockscreen_notes.di.appModule
import com.luckhost.lockscreen_notes.di.dataModule
import com.luckhost.lockscreen_notes.di.domainModule.domainCacheModule
import com.luckhost.lockscreen_notes.di.domainModule.domainModule
import com.luckhost.lockscreen_notes.di.domainModule.domainNetworkModule
import com.luckhost.lockscreen_notes.di.domainModule.domainNoteRepoModule
import com.luckhost.lockscreen_notes.di.domainModule.domainThemeModule
import org.koin.android.ext.android.inject
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
                domainCacheModule, domainNetworkModule, domainNoteRepoModule,
                domainThemeModule
                )
            )
        }


        val storage by inject<UiThemeStorage>()
        val isDark = storage.getTheme()

        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
