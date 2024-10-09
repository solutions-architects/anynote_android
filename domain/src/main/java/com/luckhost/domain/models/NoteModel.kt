package com.luckhost.domain.models

/**
 * The main Note model
 *
 * @param content the main part that contains media, markdown text etc.
 * @param hashCode is needed to perform CRUD operations in the note repository
 */
data class NoteModel(
    var content: MutableList<MutableMap<String, String>>,
    var hashCode: String?,
)