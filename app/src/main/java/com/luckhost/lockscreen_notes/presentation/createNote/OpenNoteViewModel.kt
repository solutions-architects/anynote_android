package com.luckhost.lockscreen_notes.presentation.createNote

import android.util.Log
import androidx.lifecycle.ViewModel
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase

class OpenNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val saveHashesUseCase: SaveHashesUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val changeNoteUseCase: ChangeNoteUseCase,
) : ViewModel() {
    init {
        Log.d("CreateNoteVM", "init")
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