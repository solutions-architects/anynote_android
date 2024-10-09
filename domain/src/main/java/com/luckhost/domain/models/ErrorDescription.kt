package com.luckhost.domain.models

/**
 * Error description for networking
 *
 * Has created to handle API/Network errors
 */
sealed class ErrorDescription {
    data class UnexpectedError(val message: String): ErrorDescription()
    data class NotFoundInRepoError(val message: String): ErrorDescription()
}