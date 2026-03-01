package com.luckhost.domain.repository

interface UiThemeRepoInterface {

    fun isDarkTheme(): Boolean

    fun toggleTheme()
}