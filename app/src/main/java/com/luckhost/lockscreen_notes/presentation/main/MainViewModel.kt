package com.luckhost.lockscreen_notes.presentation.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getHashesUseCase: GetHashesUseCase,
    private val deleteHashUseCase: DeleteHashUseCase,
): ViewModel() {
    private val _notesList = MutableStateFlow(mutableStateListOf<NoteModel>())
    val notesList: StateFlow<List<NoteModel>> = _notesList.asStateFlow()

    fun deleteNote(
        model: NoteModel
    ) {
        _notesList.value.remove(model)
        model.hashCode?.let {
            deleteNoteUseCase.execute(it)
            deleteHashUseCase.execute(it)
        }
    }

    fun refreshNotesList() {
        Log.d("MainVM", "start refresh")
        _notesList.value.clear()
        viewModelScope.launch {
            getNotesUseCase.execute(getHashesUseCase.execute())
                .collect{
                    value -> _notesList.value.add(value)
                    Log.d("MainVM", value.toString())
                }
        }
        Log.d("MainVM", "end refresh")
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