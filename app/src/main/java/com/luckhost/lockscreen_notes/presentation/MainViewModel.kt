package com.luckhost.lockscreen_notes.presentation

import androidx.lifecycle.ViewModel
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.GetNoteUseCase
import com.luckhost.domain.useCases.SaveNoteUseCase
import java.util.Date

class MainViewModel(
    private val getNoteUseCase: GetNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
): ViewModel() {
    fun save(
        header: String,
        content: String,
        deadLine: Date,
        coordinateX: Int,
        coordinateY: Int,
    ) {
        val modelToSave = NoteModel(
            header = header,
            content= content,
            deadLine = deadLine,
            coordinateX = coordinateX,
            coordinateY = coordinateY
        )
        saveNoteUseCase.execute(modelToSave)
    }
}