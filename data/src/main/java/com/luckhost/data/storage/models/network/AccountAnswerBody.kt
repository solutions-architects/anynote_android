package com.luckhost.data.storage.models.network

data class AccountAnswerBody(
    val id: Int?,
    val last_login: String?,
    val date_joined: String?,
    val is_superuser: Boolean?,
    val username: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val is_staff: Boolean?,
    val is_active: Boolean?,
    val groups: List<String>?,
    val user_permissions: List<String>?
)
