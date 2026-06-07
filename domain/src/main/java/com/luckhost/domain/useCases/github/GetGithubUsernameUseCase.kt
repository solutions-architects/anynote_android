package com.luckhost.domain.useCases.github

import com.luckhost.domain.repository.GithubStorageInterface

class GetGithubUsernameUseCase(private val repo: GithubStorageInterface) {
    fun execute(): String? = repo.getUsername()
}
