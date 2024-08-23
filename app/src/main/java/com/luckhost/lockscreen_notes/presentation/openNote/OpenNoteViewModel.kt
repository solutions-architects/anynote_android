package com.luckhost.lockscreen_notes.presentation.openNote

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.AddHashUseCase
import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.di.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpenNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val changeNoteUseCase: ChangeNoteUseCase,
    private val addHashUseCase: AddHashUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private lateinit var note: NoteModel

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _titleTextState = MutableStateFlow("")
    val titleTextState: StateFlow<String> = _titleTextState.asStateFlow()

    private val _mainPartState =
        MutableStateFlow(mutableStateListOf<MutableMap<String, String>>())
    val mainPartState: StateFlow<MutableList<MutableMap<String, String>>> =
        _mainPartState.asStateFlow()

    fun changeEditModeState() {
        _isEditMode.value = !_isEditMode.value
    }

    fun hideDialogState() {
        _showDialog.value = false
    }
    fun showDialogState() {
        _showDialog.value = true
    }

    fun createEmptyNote() {
        val infoBlock = mutableMapOf("name" to "info",
            "header" to resourceProvider.getString(R.string.empty_note_title))
        val essenceBlock = mutableMapOf("name" to "md",
            "text" to resourceProvider.getString(R.string.empty_note_content))

        _titleTextState.value = resourceProvider.getString(R.string.empty_note_title)
        _mainPartState.value = mutableStateListOf(infoBlock, essenceBlock)

        note = NoteModel(
            content = _mainPartState.value,
            hashCode = null
        )

        saveNoteUseCase.execute(note)
        note.hashCode = note.hashCode()
        addHashUseCase.execute(note.hashCode!!)
    }

    fun getNote(hashCode: Int) {
        viewModelScope.launch {
            Log.d("OpenNoteVM", hashCode.toString())
            note = getNotesUseCase.execute(listOf(hashCode)).first()

            Log.d("OpenNoteVM", note.hashCode.toString())
            Log.d("OpenNoteVM", note.content.toString())
            withContext(Dispatchers.Main) {
                _mainPartState.value = note.content.toMutableStateList()
                note.content.forEach{
                    if (it["name"] == "info") {
                        _titleTextState.value = it["header"].toString()
                    }
                }
            }
        }
    }

    fun returnOldValues() {
        note.hashCode?.let {
            getNote(it)
        } ?: {
            throw NullPointerException("Note hash was null!")
        }
    }

    fun updateTitleStateText(newTitle: String) {
        _titleTextState.value = newTitle
    }

    fun updateMdStateText(index: Int, newText: String) {
        _mainPartState.value[index]["text"] = newText
    }

    fun saveChanges() {
        note.hashCode?.let {
            note.content = _mainPartState.value
            note.content[0]["header"] = _titleTextState.value
            changeNoteUseCase.execute(it, note)
        } ?: {
            Log.d("OpenNoteVM", "The changes is not saved, hashcode is null")
        }
    }
}