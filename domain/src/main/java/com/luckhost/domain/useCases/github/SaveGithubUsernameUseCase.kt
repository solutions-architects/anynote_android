package com.luckhost.domain.useCases.github

import com.luckhost.domain.repository.GithubStorageInterface

class SaveGithubUsernameUseCase(private val repo: GithubStorageInterface) {
    fun execute(username: String) = repo.saveUsername(username)
}
