package com.luckhost.lockscreen_notes.presentation.openNote.additional.models

data class NoteBoxModel(
    var title: String,
    var mdText: String,
    var imageSource: String?,
    val parentHash: String,
)
