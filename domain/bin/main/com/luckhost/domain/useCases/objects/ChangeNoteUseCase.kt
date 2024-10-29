package com.luckhost.domain.useCases.objects

import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface

class ChangeNoteUseCase(
    private val repository: NotesRepositoryInterface,
) {
    fun execute(
        noteHash: String,
        saveObject: NoteModel,
    ) {
        repository.changeNote(noteHash, saveObject)
    }
}