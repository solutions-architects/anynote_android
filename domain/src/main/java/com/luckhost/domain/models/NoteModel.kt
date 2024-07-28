package com.luckhost.domain.models


class NoteModel(
    var content: MutableList<MutableMap<String, String>>,
    var hashCode: Int?,
) {
}