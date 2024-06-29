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
            header = saveObject.header,
            content= saveObject.content,
            deadLine = saveObject.deadLine,
            coordinateX = saveObject.coordinateX,
            coordinateY = saveObject.coordinateY,
            noteHash = saveObject.hashCode()
        )
        notesStorage.saveNote(note)
    }

    override fun getNote(noteHash: Int): NoteModel {
        val result = notesStorage.getNote(noteHash)
        return NoteModel(
            header = result.header,
            content= result.content,
            deadLine = result.deadLine,
            coordinateX = result.coordinateX,
            coordinateY = result.coordinateY,
        )
    }
}