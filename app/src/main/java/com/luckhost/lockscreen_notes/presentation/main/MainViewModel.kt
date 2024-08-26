package com.luckhost.lockscreen_notes.presentation.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.useCases.keys.DeleteHashUseCase
import com.luckhost.domain.useCases.keys.GetHashesUseCase
import com.luckhost.domain.useCases.network.localActions.GetLocalAuthTokenUseCase
import com.luckhost.domain.useCases.network.localActions.SaveLocalAuthTokenUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.lockscreen_notes.presentation.main.additional.functions.extractAndFilter
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteActivity
import com.luckhost.lockscreen_notes.presentation.openNote.additional.models.NoteBoxModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getHashesUseCase: GetHashesUseCase,
    private val deleteHashUseCase: DeleteHashUseCase,
    private val getLocalAuthTokenUseCase: GetLocalAuthTokenUseCase,
    private val saveLocalAuthTokenUseCase: SaveLocalAuthTokenUseCase,
): ViewModel() {
    private var accessTokens: AuthToken = AuthToken(accessToken = null, refreshToken = null)

    private val _notesList = MutableStateFlow(mutableStateListOf<NoteModel>())

    private val _noteBoxesList = MutableStateFlow(mutableStateListOf<NoteBoxModel>())
    val noteBoxesList: StateFlow<List<NoteBoxModel>> = _noteBoxesList.asStateFlow()

    init {
        val localSavedTokens = getLocalAuthTokenUseCase.execute()

        when(localSavedTokens) {
            is Either.Right -> {
                accessTokens = localSavedTokens.b
            }

            is Either.Left -> {

            }
        }
    }


    fun deleteNote(
        noteHash: String
    ) {

        val noteHashInt = noteHash.toInt()

        val noteToDelete = _notesList.value.find { predicate ->
            predicate.hashCode?.equals(noteHashInt)
                ?: throw NoSuchElementException("no note with such a hashcode was found")
        }
        val noteBoxToDelete = _noteBoxesList.value.find { predicate ->
            predicate.parentHash.equals(noteHash)
        }

        _notesList.value.remove(noteToDelete)
        _noteBoxesList.value.remove(noteBoxToDelete)

        noteHashInt.let {
            deleteNoteUseCase.execute(it)
            deleteHashUseCase.execute(it)
        }
    }

    fun refreshNotesList() {
        _notesList.value.clear()
        _noteBoxesList.value.clear()

        viewModelScope.launch {
            _notesList.value = getNotesUseCase.execute(getHashesUseCase.execute())
                .toMutableStateList()

            withContext(Dispatchers.Main) {
                _notesList.value.forEach {
                    createNoteBoxModels(it)
                }
            }
        }
    }

    private fun createNoteBoxModels(noteModel: NoteModel) {
        val result = NoteBoxModel("", "", null,
            noteModel.hashCode.toString())

        for (entry in noteModel.content) {
            when (entry["name"]) {
                "info" -> {
                    if (result.title.isNotEmpty()) {
                        Log.e("MainView_NoteBox", "There is two titles in note!")
                    }

                    entry["header"]?.let {
                        result.title = it
                    }
                }
                "md" -> {
                    if (result.mdText.isNotEmpty()) continue

                    entry["text"]?.let {
                        extractAndFilter(it)
                        val filteredString = extractAndFilter(it)

                        result.mdText = filteredString.first
                        filteredString.second?.let { it1 -> result.imageSource = it1 }
                    }
                }
                "map" -> { /* TODO */ }
            }
        }
        _noteBoxesList.value.add(result)
    }

    fun startOpenNoteActivity(context: Context) {
        val intent = Intent(context, OpenNoteActivity::class.java)
        context.startActivity(intent)
    }

    fun startOpenNoteActivity(context: Context, noteHash: String) {
        val intent = Intent(context, OpenNoteActivity::class.java)
        intent.putExtra("noteHash", noteHash)
        context.startActivity(intent)
    }
}