package com.luckhost.domain.useCases

import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesHashesRepoInterface
import com.luckhost.domain.repository.NotesRepositoryInterface
import java.util.Date

class GetNoteUseCase(
    private val repository: NotesRepositoryInterface,
    private val repositoryKeys: NotesHashesRepoInterface,
) {
    fun execute(): NoteModel {
        val hashes = repositoryKeys.getHashes()
        if(hashes.isNotEmpty()) {
            return repository.getNote(hashes.first())
        }
        return NoteModel("","",Date(),0,0)
    }
}