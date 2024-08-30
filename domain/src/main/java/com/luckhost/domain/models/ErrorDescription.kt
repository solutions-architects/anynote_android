package com.luckhost.domain.models

sealed class ErrorDescription {
    data class UnexpectedError(val message: String): ErrorDescription()
    data class NotFoundInRepoError(val message: String): ErrorDescription()
}