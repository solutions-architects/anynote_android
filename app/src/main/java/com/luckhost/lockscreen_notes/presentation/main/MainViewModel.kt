package com.luckhost.lockscreen_notes.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import java.util.Date

class MainViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getHashesUseCase: GetHashesUseCase,
    private val deleteHashUseCase: DeleteHashUseCase,
): ViewModel() {
    private var notesList: MutableList<NoteModel> = mutableListOf()

    fun deleteNote(
        hashCode: Int
    ) {
        deleteNoteUseCase.execute(hashCode)
        deleteHashUseCase.execute(hashCode)
    }

    fun getNotes(): List<NoteModel> {
        notesList = getNotesUseCase.execute(
            getHashesUseCase.execute().toMutableList()
        ).toMutableList()
        return notesList
    }
}