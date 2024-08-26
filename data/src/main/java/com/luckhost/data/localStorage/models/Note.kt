package com.luckhost.data.localStorage.models

data class Note(
    var serverId: Int?,
    var content: List<MutableMap<String, String>>,
    val noteHash: Int,
)