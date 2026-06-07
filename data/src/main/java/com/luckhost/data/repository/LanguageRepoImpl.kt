package com.luckhost.data.repository

import com.luckhost.data.localStorage.settings.LanguageStorage
import com.luckhost.domain.repository.LanguageRepoInterface

class LanguageRepoImpl(private val storage: LanguageStorage) : LanguageRepoInterface {
    override fun getLanguage(): String = storage.getLanguage()
    override fun setLanguage(code: String) = storage.setLanguage(code)
}
