package com.luckhost.data.repository

import com.luckhost.data.storage.Note
import com.luckhost.data.storage.NotesStorage
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.repository.NotesRepositoryInterface
import java.util.Date

open class NotesRepositoryImpl(
    private val notesStorage: NotesStorage,
): NotesRepositoryInterface {
    override fun saveNote(saveParam: NoteModel) {
        val note = Note(
            header = saveParam.header,
            content= saveParam.content,
            deadLine = saveParam.deadLine,
            coordinateX = saveParam.coordinateX,
            coordinateY = saveParam.coordinateY
        )


    }

    override fun getNote(): NoteModel {
        return NoteModel(
            header = "",
            content= "saveParam.content",
            deadLine = Date(),
            coordinateX = 0,
            coordinateY = 0
        )
    }
}