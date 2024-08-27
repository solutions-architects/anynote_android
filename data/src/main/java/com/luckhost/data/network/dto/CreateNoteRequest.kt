package com.luckhost.data.network.dto

import com.google.gson.annotations.SerializedName

data class CreateNoteRequest(
    var content: List<MutableMap<String, String>>,
    @SerializedName("hash")
    val noteHash: String? = null,
    val user: Int,
)
