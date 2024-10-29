package com.luckhost.domain.useCases.objects

import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface

class GetNotesUseCase(
    private val repository: NotesRepositoryInterface,
) {
    suspend fun execute(): List<NoteModel> {
        return repository.getNotes()
    }
}