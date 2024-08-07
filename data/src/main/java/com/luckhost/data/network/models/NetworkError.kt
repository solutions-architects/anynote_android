package com.luckhost.data.network.models

sealed class NetworkError {
    data class Api(val error: Map<String, String>) : NetworkError()
    data class Unexpected(val error: String) : NetworkError()
}