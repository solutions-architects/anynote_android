package com.luckhost.domain.useCases.github

import com.luckhost.domain.repository.GithubStorageInterface

class ClearGithubConnectionUseCase(private val repo: GithubStorageInterface) {
    fun execute() = repo.clear()
}
