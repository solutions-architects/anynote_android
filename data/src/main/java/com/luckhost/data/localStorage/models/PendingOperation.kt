package com.luckhost.data.localStorage.models

data class PendingOperation(
    val noteHash: String,
    val type: OperationType
)

enum class OperationType {
    CREATE,
    UPDATE,
    DELETE
}
