package com.luckhost.data.repository

import com.luckhost.data.storage.models.Note
import com.luckhost.data.storage.materials.NotesStorage
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface

open class NotesRepositoryImpl(
    private val notesStorage: NotesStorage,
): NotesRepositoryInterface {
    override fun saveNote(saveObject: NoteModel) {
        val note = Note(
            content= saveObject.content,
            noteHash = saveObject.hashCode()
        )
        notesStorage.saveNote(note)
    }

    override fun getNotes(noteHashes: List<Int>): List<NoteModel> {
        val getData = notesStorage.getNotes(noteHashes)
        val result: MutableList<NoteModel> = mutableListOf()
        getData.forEach{
            result.add(
                NoteModel(
                    content = it.content.toMutableList(),
                    hashCode = it.noteHash,
                )

            )
        }
        return result
    }

    override fun deleteNote(noteHash: Int) {
        notesStorage.deleteNote(noteHash)
    }

    override fun changeNote(noteHash: Int, saveObject: NoteModel) {
        val note = Note(
            content= saveObject.content,
            noteHash = noteHash
        )
        notesStorage.changeNote(noteHash, note)
    }
}