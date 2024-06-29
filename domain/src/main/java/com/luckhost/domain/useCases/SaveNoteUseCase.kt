package com.luckhost.domain.useCases

import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface

class SaveNoteUseCase(
    private val repository: NotesRepositoryInterface
) {
    fun execute(
        saveObject: NoteModel,
    ) {
        repository.saveNote(saveObject)
    }
}