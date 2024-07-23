package com.luckhost.lockscreen_notes.presentation.createNote

import android.util.Log
import androidx.lifecycle.ViewModel
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.AddHashUseCase
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import java.util.Date

class OpenNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val changeNoteUseCase: ChangeNoteUseCase,
    private val addHashUseCase: AddHashUseCase,
) : ViewModel() {
    init {
        Log.d("CreateNoteVM", "init")
    }

    fun createNote(
        header: String,
        content: String,
        deadLine: Date,
        coordinateX: Int,
        coordinateY: Int,
    ): NoteModel {
        val modelToSave = NoteModel(
            header = header,
            content = content,
            deadLine = deadLine,
            coordinateX = coordinateX,
            coordinateY = coordinateY,
            hashCode = null
        )
        saveNoteUseCase.execute(modelToSave)
        addHashUseCase.execute(modelToSave.hashCode())

        return NoteModel(
            header = header,
            content = content,
            deadLine = deadLine,
            coordinateX = coordinateX,
            coordinateY = coordinateY,
            hashCode = modelToSave.hashCode()
        )
    }

    fun getNote(hashCode: Int): NoteModel {
        return getNotesUseCase.execute(listOf(hashCode)).first()
    }

    fun saveNote(note: NoteModel) {
        saveNoteUseCase.execute(note)
    }

    fun changeNote(noteHash: Int, note: NoteModel) {
        changeNoteUseCase.execute(noteHash, note)
    }
}