package com.luckhost.data.network.dto

data class CreateNoteRequest(
    var content: List<MutableMap<String, String>>,
    val noteHash: String,
)
