package com.luckhost.lockscreen_notes.di

import android.content.Context

class ResourceProvider(private val context: Context) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}
