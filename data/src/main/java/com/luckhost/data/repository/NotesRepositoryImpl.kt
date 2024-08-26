package com.luckhost.data.repository

import com.luckhost.data.localStorage.models.Note
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

open class NotesRepositoryImpl(
    private val notesStorage: NotesStorage,
): NotesRepositoryInterface {
    override fun saveNote(saveObject: NoteModel) {
        val note = Note(
            serverId = null,
            content= saveObject.content,
            noteHash = saveObject.hashCode()
        )
        notesStorage.saveNote(note)
    }

    override suspend fun getNotes(noteHashes: List<Int>): List<NoteModel> {
        return withContext(Dispatchers.IO) {
            notesStorage.getNotes(noteHashes)
                .map { note ->
                    NoteModel(
                        content = note.content.toMutableList(),
                        hashCode = note.noteHash,
                    )
                }.toList()
        }
    }

    override fun deleteNote(noteHash: Int) {
        notesStorage.deleteNote(noteHash)
    }

    override fun changeNote(noteHash: Int, saveObject: NoteModel) {
        val note = Note(
            serverId = null,
            content= saveObject.content,
            noteHash = noteHash
        )
        notesStorage.changeNote(noteHash, note)
    }
}