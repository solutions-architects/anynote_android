package com.luckhost.domain.repository

interface LanguageRepoInterface {
    fun getLanguage(): String
    fun setLanguage(code: String)
}
