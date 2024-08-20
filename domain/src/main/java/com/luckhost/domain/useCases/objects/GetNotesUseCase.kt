package com.luckhost.domain.useCases.objects

import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(
    private val repository: NotesRepositoryInterface,
) {
    suspend fun execute(
        noteHashes: List<Int>
    ): Flow<NoteModel> {
        return repository.getNotes(noteHashes)
    }
}