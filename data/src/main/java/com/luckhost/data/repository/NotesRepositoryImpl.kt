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

    override fun getNotes(noteHashes: List<Int>): List<NoteModel> {
        val getData = notesStorage.getNotes(noteHashes)
        val result: MutableList<NoteModel> = mutableListOf()
        getData.forEach{
            result.add(
                NoteModel(
                    header = it.header,
                    content= it.content,
                    deadLine = it.deadLine,
                    coordinateX = it.coordinateX,
                    coordinateY = it.coordinateY,
                )
            )
        }
        return result
    }

    override fun deleteNote(noteHash: Int) {
        notesStorage.deleteNote(noteHash)
    }
}