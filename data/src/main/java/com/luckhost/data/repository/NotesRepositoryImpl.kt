package com.luckhost.data.repository

import android.util.Log
import com.luckhost.data.localStorage.models.Note
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class NotesRepositoryImpl(
    private val notesStorage: NotesStorage,
): NotesRepositoryInterface {
    override fun saveNote(saveObject: NoteModel) {
        val note = Note(
            id = null,
            content= saveObject.content,
            noteHash = saveObject.hashCode()
        )
        notesStorage.saveNote(note)
    }

    override suspend fun getNotes(noteHashes: List<Int>) = flow<NoteModel> {
        notesStorage.getNotes(noteHashes)
            .onEach { note ->
                emit(
                    NoteModel(
                        content = note.content.toMutableList(),
                        hashCode = note.noteHash,
                    )
                )
            }.collect()
    }

    override fun deleteNote(noteHash: Int) {
        notesStorage.deleteNote(noteHash)
    }

    override fun changeNote(noteHash: Int, saveObject: NoteModel) {
        val note = Note(
            id = null,
            content= saveObject.content,
            noteHash = noteHash
        )
        notesStorage.changeNote(noteHash, note)
    }
}