package com.luckhost.lockscreen_notes.presentation.openNote

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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

    private val _showSaveChangesDialog = MutableStateFlow(false)
    val showSaveChangesDialog: StateFlow<Boolean> = _showSaveChangesDialog.asStateFlow()

    private val _showDeleteImageDialog = MutableStateFlow(false)
    val showDeleteImageDialog: StateFlow<Boolean> = _showDeleteImageDialog.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _titleTextState = MutableStateFlow("")
    val titleTextState: StateFlow<String> = _titleTextState.asStateFlow()

    private val _textFieldStates = MutableStateFlow<Map<Int, TextFieldValue>>(emptyMap())
    val textFieldStates: StateFlow<Map<Int, TextFieldValue>> = _textFieldStates

    private val _mainPartState =
        mutableStateListOf<MutableMap<String, String>>()
    val mainPartState: SnapshotStateList<MutableMap<String, String>> = _mainPartState

    private val _mediaGetResult = MutableStateFlow("")
    val mediaGetResult: StateFlow<String> = _mediaGetResult.asStateFlow()

    fun changeEditModeState() {
        _isEditMode.value = !_isEditMode.value
    }

    fun hideSaveChangesDialogState() { _showSaveChangesDialog.value = false }
    fun showSaveChangesDialogState() { _showSaveChangesDialog.value = true }

    fun hideDeleteImageDialogState() { _showDeleteImageDialog.value = false }
    fun showDeleteImageDialogState() { _showDeleteImageDialog.value = true }

    fun updateTextFieldState(index: Int, newText: TextFieldValue) {
        _textFieldStates.value = _textFieldStates.value.toMutableMap().apply {
            this[index] = newText
        }
    }

    fun updateTitleStateText(newTitle: String) {
        _titleTextState.value = newTitle
    }

    fun updateMdStateText(index: Int, newText: String) {
        _mainPartState[index]["text"] = newText
    }

    fun changeMediaGetResult(newText: String) {
        _mediaGetResult.value = newText
    }

    fun returnOldValues() {
        note.hashCode?.let {
            _mainPartState.clear()
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
        _mainPartState.add(infoBlock)
        _mainPartState.add(essenceBlock)
        _textFieldStates.value = mapOf(
            1 to TextFieldValue(resourceProvider.getString(R.string.empty_note_content)))

        note = NoteModel(
            content = _mainPartState,
            hashCode = null
        )

        saveNoteUseCase.execute(note)
        note.hashCode = note.hashCode().toString()
    }

    fun getNote(hashCode: String) {
        viewModelScope.launch {
            note = getNoteByHashUseCase.execute(hashCode)
            withContext(Dispatchers.Main) {
                _mainPartState.addAll(note.content.toList())

                // Adding states
                note.content.forEachIndexed { index, it ->
                    when(it["name"]) {
                        "info" -> {
                            _titleTextState.value = it["header"].toString()
                        }
                        "md" -> {
                            _textFieldStates.value = _textFieldStates.value.toMutableMap().apply {
                                it["text"]?.let { text ->
                                    this[index] = TextFieldValue(text)
                                }
                            }
                        }
                        "image" -> {

                        }
                    }
                }
            }
        }
    }

    fun saveChanges() {
        note.hashCode?.let {
            note.content = _mainPartState
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
        _mainPartState[index]["text"] = beforeText
        updateTextFieldState(index, TextFieldValue(beforeText))

        _mainPartState.add(
            index + 1, mutableMapOf(
            "name" to "image",
            "mediaLink" to _mediaGetResult.value))
        _mainPartState.add(
            index + 2, mutableMapOf(
                "name" to "md", "text" to afterText))

        updateTextFieldState(index + 2, TextFieldValue(afterText))

        _mainPartState.forEachIndexed { counter, content ->
            if (content["name"] == "md") {
                _textFieldStates.value = _textFieldStates.value.toMutableMap().apply {
                    content["text"]?.let { text ->
                        this[counter] = TextFieldValue(text)
                    }
                }
            }
        }
    }

    fun deleteImageAndMergeText(
        index: Int,
    ) {
        if (_mainPartState[index]["name"] != "image") {
            throw NoSuchElementException("No such image by this id!")
        }

        if (_mainPartState.size > index + 1 &&
            index > 0 &&
            _mainPartState[index - 1]["name"] == "md" &&
            _mainPartState[index + 1]["name"] == "md"
            ) {
            val newText =
                _mainPartState[index - 1]["text"] + _mainPartState[index + 1]["text"]
            _mainPartState[index - 1]["text"] = newText
            updateTextFieldState(index -1, TextFieldValue(newText))

            _mainPartState.removeAt(index + 1)
        }

        _mainPartState.removeAt(index)

        _mainPartState.forEachIndexed { counter, content ->
            if (content["name"] == "md") {
                _textFieldStates.value = _textFieldStates.value.toMutableMap().apply {
                    content["text"]?.let { text ->
                        this[counter] = TextFieldValue(text)
                    }
                }
            }
        }
    }

    fun getRealPathFromUri(uri: Uri): String {
        return getCachedImageLinkUseCase.execute(uri.toString())
    }
}