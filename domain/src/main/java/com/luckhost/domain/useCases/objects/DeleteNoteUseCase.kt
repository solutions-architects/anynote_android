package com.luckhost.domain.useCases.objects

import com.luckhost.domain.repository.NoteHashesRepoInterface
import com.luckhost.domain.repository.NotesRepositoryInterface

class DeleteNoteUseCase(
    private val repository: NotesRepositoryInterface,
) {
    fun execute(noteHash: Int) {
        repository.deleteNote(noteHash)
    }
}