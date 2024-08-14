package com.luckhost.lockscreen_notes.presentation.main

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteActivity

class MainViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getHashesUseCase: GetHashesUseCase,
    private val deleteHashUseCase: DeleteHashUseCase,
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
): ViewModel() {
    var notesList = mutableStateListOf<NoteModel>()

    fun deleteNote(
        model: NoteModel
    ) {
        notesList.remove(model)
        model.hashCode?.let {
            deleteNoteUseCase.execute(it)
            deleteHashUseCase.execute(it)
        }
    }

    fun refreshNotesList() {
        notesList.clear()
        notesList.addAll(getNotesUseCase.execute(
            getHashesUseCase.execute().toMutableList()
        ).toMutableStateList())


    }

    fun startOpenNoteActivity(context: Context) {
        val intent = Intent(context, OpenNoteActivity::class.java)
        context.startActivity(intent)
    }

    fun startOpenNoteActivity(context: Context, note: NoteModel) {
        val intent = Intent(context, OpenNoteActivity::class.java)
        intent.putExtra("noteHash", note.hashCode)
        context.startActivity(intent)
    }
}