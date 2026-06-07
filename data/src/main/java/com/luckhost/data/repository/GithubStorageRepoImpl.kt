package com.luckhost.data.repository

import com.luckhost.data.localStorage.keys.github.SharedPrefGithubStorage
import com.luckhost.domain.repository.GithubStorageInterface

class GithubStorageRepoImpl(private val storage: SharedPrefGithubStorage) : GithubStorageInterface {
    override fun saveUsername(username: String) = storage.saveUsername(username)
    override fun getUsername(): String? = storage.getUsername()
    override fun clear() = storage.clear()
}
