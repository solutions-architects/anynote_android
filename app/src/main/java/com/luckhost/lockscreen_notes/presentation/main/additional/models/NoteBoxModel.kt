package com.luckhost.lockscreen_notes.presentation.main.additional.models

import kotlinx.coroutines.flow.StateFlow

/**
 * A model containing all the necessary information for the NoteBox
 *
 * It appeared due to the fact that during rendering it was constantly necessary to parse,
 * shorten the text and select media to display in the NoteBox.
 * Now this happens once when creating a NoteBox
 */
data class NoteBoxModel(
    var title: String,
    var mdText: String,
    var imageSource: String?,
    val parentHash: String,
    val visible: StateFlow<Boolean>
)
