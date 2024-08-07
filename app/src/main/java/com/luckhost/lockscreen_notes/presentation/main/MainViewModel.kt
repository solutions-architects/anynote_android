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
import com.luckhost.domain.useCases.objects.GetAuthTokenUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getHashesUseCase: GetHashesUseCase,
    private val deleteHashUseCase: DeleteHashUseCase,
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
): ViewModel() {
    var notesList = mutableStateListOf<NoteModel>()

    init{
        getToken()
    }

    fun getToken() = runBlocking{
        viewModelScope.launch {
            try {
                // Выполняем длительную операцию в Dispatchers.IO (поток ввода-вывода)
                val token = withContext(Dispatchers.IO) {
                    getAuthTokenUseCase.execute()
                }
                Log.d("MainVM", token.toString())
            } catch (e: Exception) {
                Log.e("MainVM", "Error getting token", e)
            }
        }
    }

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