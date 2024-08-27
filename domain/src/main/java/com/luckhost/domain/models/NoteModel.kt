package com.luckhost.domain.models


data class NoteModel(
    var content: MutableList<MutableMap<String, String>>,
    var hashCode: String?,
)