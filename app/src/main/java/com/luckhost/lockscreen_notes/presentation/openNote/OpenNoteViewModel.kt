package com.luckhost.lockscreen_notes.presentation.openNote

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.cache.GetCachedImageLinkUseCase
import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.GetNoteByHashUseCase
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
    private val getNoteByHashUseCase: GetNoteByHashUseCase,
    private val changeNoteUseCase: ChangeNoteUseCase,
    private val getCachedImageLinkUseCase: GetCachedImageLinkUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private lateinit var note: NoteModel

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _titleTextState = MutableStateFlow("")
    val titleTextState: StateFlow<String> = _titleTextState.asStateFlow()

    private val _textFieldStates = MutableStateFlow<Map<Int, TextFieldValue>>(emptyMap())
    val textFieldStates: StateFlow<Map<Int, TextFieldValue>> = _textFieldStates

    private val _mainPartState =
        MutableStateFlow(mutableStateListOf<MutableMap<String, String>>())
    val mainPartState: StateFlow<MutableList<MutableMap<String, String>>> =
        _mainPartState.asStateFlow()

    private val _mediaGetResult = MutableStateFlow("")
    val mediaGetResult: StateFlow<String> = _mediaGetResult.asStateFlow()

    fun changeEditModeState() {
        _isEditMode.value = !_isEditMode.value
    }

    fun hideDialogState() {
        _showDialog.value = false
    }

    fun showDialogState() {
        _showDialog.value = true
    }

    fun updateTextFieldState(index: Int, newText: TextFieldValue) {
        _textFieldStates.value = _textFieldStates.value.toMutableMap().apply {
            this[index] = newText
        }
    }

    fun updateTitleStateText(newTitle: String) {
        _titleTextState.value = newTitle
    }

    fun updateMdStateText(index: Int, newText: String) {
        _mainPartState.value[index]["text"] = newText
    }

    fun changeMediaGetResult(newText: String) {
        _mediaGetResult.value = newText
    }

    fun returnOldValues() {
        note.hashCode?.let {
            getNote(it)
        } ?: {
            throw NullPointerException("Note hash was null!")
        }
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
        note.hashCode = note.hashCode().toString()
    }

    fun getNote(hashCode: String) {
        viewModelScope.launch {
            note = getNoteByHashUseCase.execute(hashCode)
            withContext(Dispatchers.Main) {
                _mainPartState.value = note.content.toMutableStateList()
                note.content.forEachIndexed { index, it ->
                    if (it["name"] == "info") {
                        _titleTextState.value = it["header"].toString()
                    }

                    if (it["name"] == "md") {
                        _textFieldStates.value = _textFieldStates.value.toMutableMap().apply {
                            it["text"]?.let { text ->
                                this[index] = TextFieldValue(text)
                            }
                        }
                    }
                }
            }
        }
    }

    fun saveChanges() {
        note.hashCode?.let {
            note.content = _mainPartState.value
            note.content[0]["header"] = _titleTextState.value
            changeNoteUseCase.execute(it, note)
        } ?: {
            throw NullPointerException("The changes is not saved, hashcode is null")
        }
    }

    fun splitTextFieldWithImage(
        index: Int,
        beforeText: String,
        afterText: String,
    ) {
        Log.d("OpenNoteViewModel", "Splitting text at index: $index")
        Log.d("OpenNoteViewModel", "Before text: $beforeText")
        Log.d("OpenNoteViewModel", "After text: $afterText")

        _mainPartState.value[index]["text"] = beforeText

        val newList = _mainPartState.value.toMutableList()
        newList.add(index + 1, mutableMapOf("name" to "image", "mediaLink" to _mediaGetResult.value))
        newList.add(index + 2, mutableMapOf("name" to "md", "text" to afterText))

        _mainPartState.value = mutableStateListOf(*newList.toTypedArray())

        Log.d("OpenNoteViewModel", "New mainPartState: $newList")
    }


    fun getRealPathFromUri(uri: Uri): String {
        return getCachedImageLinkUseCase.execute(uri.toString())
    }
}