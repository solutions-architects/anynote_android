package com.luckhost.domain.useCases.objects

import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface

class GetNoteByHashUseCase(
    private val repository: NotesRepositoryInterface,
) {
    fun execute(hash: String): NoteModel {
        return repository.getNoteByHash(hash)
    }
}