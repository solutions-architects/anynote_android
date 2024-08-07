package com.luckhost.domain.models.network

sealed class NetworkErrorDescription {
    data class Api(val error: MutableMap<String, String>) : NetworkErrorDescription()
    data class Unexpected(val error: String) : NetworkErrorDescription()
}