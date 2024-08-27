package com.luckhost.lockscreen_notes.presentation.openNote.additional.models

import kotlinx.coroutines.flow.StateFlow

data class NoteBoxModel(
    var title: String,
    var mdText: String,
    var imageSource: String?,
    val parentHash: String,
    val visible: StateFlow<Boolean>
)
