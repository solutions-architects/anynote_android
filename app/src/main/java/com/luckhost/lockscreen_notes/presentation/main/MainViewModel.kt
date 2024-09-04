package com.luckhost.lockscreen_notes.presentation.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.useCases.cache.DeleteCachedImagesUseCase
import com.luckhost.domain.useCases.filters.GetFilteredMdAndFirstImgUseCase
import com.luckhost.domain.useCases.network.localActions.GetLocalAuthTokenUseCase
import com.luckhost.domain.useCases.network.localActions.SaveLocalAuthTokenUseCase
import com.luckhost.domain.useCases.objects.DeleteNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.di.ResourceProvider
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteActivity
import com.luckhost.lockscreen_notes.presentation.openNote.additional.models.NoteBoxModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getLocalAuthTokenUseCase: GetLocalAuthTokenUseCase,
    private val saveLocalAuthTokenUseCase: SaveLocalAuthTokenUseCase,
    private val deleteCachedImagesUseCase: DeleteCachedImagesUseCase,
    private val getFilteredMdAndFirstImgUseCase: GetFilteredMdAndFirstImgUseCase,
    private val resourceProvider: ResourceProvider,
): ViewModel() {
    private var accessTokens: AuthToken = AuthToken(accessToken = null, refreshToken = null)

    private val _notesList = MutableStateFlow(mutableStateListOf<NoteModel>())

    private val _noteBoxesList = MutableStateFlow(mutableStateListOf<NoteBoxModel>())
    val noteBoxesList: StateFlow<List<NoteBoxModel>> = _noteBoxesList.asStateFlow()

    private val _toastNotification = MutableStateFlow("")
    val toastNotification: StateFlow<String> = _toastNotification.asStateFlow()

    private val _noteBoxesVisibleStateList =
        mutableStateMapOf<String, MutableStateFlow<Boolean>>()

    init {
        val localSavedTokens = getLocalAuthTokenUseCase.execute()

        when(localSavedTokens) {
            is Either.Right -> {
                accessTokens = localSavedTokens.b
            }
            is Either.Left -> {
                _toastNotification.value = resourceProvider.getString(
                    R.string.main_activity_authorization_failed)
            }
        }
    }

    fun clearToastNotification() {
        _toastNotification.value = ""
    }

    fun deleteNote(
        noteHash: String
    ) {
        val noteToDelete = _notesList.value.find { predicate ->
            predicate.hashCode?.equals(noteHash) == true
        }

        if (noteToDelete == null) {
            throw IllegalArgumentException("no note with such a hashcode was found")
        }

        val noteBoxToDelete = _noteBoxesList.value.find { predicate ->
            predicate.parentHash == noteHash
        }

        _noteBoxesVisibleStateList[noteHash]?.value = false

        viewModelScope.launch {
            delay(300)

            for (entry in noteToDelete.content) {
                when (entry["name"]) {
                    "md" -> {
                        entry["text"]?.let {
                            val filteredObjects = getFilteredMdAndFirstImgUseCase.execute(it)

                            deleteCachedImagesUseCase.execute(filteredObjects.second)
                        }
                    }
                }
            }

            _notesList.value.remove(noteToDelete)
            _noteBoxesList.value.remove(noteBoxToDelete)

            deleteNoteUseCase.execute(noteHash)
        }
    }

    fun refreshNotesList() {
        _notesList.value.clear()
        _noteBoxesList.value.clear()

        var delayBeforeAnimation: Long = 50

        viewModelScope.launch {
            _notesList.value = getNotesUseCase.execute()
                .toMutableStateList()

            withContext(Dispatchers.Main) {
                _notesList.value.forEach {
                    createNoteBoxModels(it, delayBeforeAnimation)
                    delayBeforeAnimation += 25
                }
            }
        }
    }

    private fun createNoteBoxModels(noteModel: NoteModel, delayBeforeAnimation: Long) {
        val noteHash = noteModel.hashCode.toString()

        _noteBoxesVisibleStateList[noteHash] = MutableStateFlow(false)

        val result = NoteBoxModel("", "", null,
            noteHash, _noteBoxesVisibleStateList[noteHash]!!.asStateFlow())

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
                        val filteredObjects = getFilteredMdAndFirstImgUseCase.execute(it)

                        result.mdText = filteredObjects.first
                        if (filteredObjects.second.isNotEmpty()) {
                            result.imageSource = filteredObjects.second.first()
                        }
                    }
                }
                "map" -> { /* TODO */ }
            }
        }
        _noteBoxesList.value.add(result)

        viewModelScope.launch {
            delay(delayBeforeAnimation)
            _noteBoxesVisibleStateList[noteHash]!!.value = true
        }
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