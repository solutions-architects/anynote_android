package com.luckhost.domain.repository

interface GithubStorageInterface {
    fun saveUsername(username: String)
    fun getUsername(): String?
    fun clear()
}
