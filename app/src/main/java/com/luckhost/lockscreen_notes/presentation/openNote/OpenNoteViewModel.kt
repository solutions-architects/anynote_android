package com.luckhost.lockscreen_notes.presentation.openNote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.useCases.keys.AddHashUseCase
import com.luckhost.domain.useCases.objects.ChangeNoteUseCase
import com.luckhost.domain.useCases.objects.GetNotesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.di.ResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OpenNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val changeNoteUseCase: ChangeNoteUseCase,
    private val addHashUseCase: AddHashUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    lateinit var note: NoteModel
    lateinit var titleText: String

    fun changeEditModeState() {
        _isEditMode.value = !_isEditMode.value
    }

    fun createEmptyNote() {
        titleText = resourceProvider.getString(R.string.empty_note_title)
        val contentText = resourceProvider.getString(R.string.empty_note_content)
        val infoBlock = mutableMapOf<String, String>("name" to "info",
            "header" to titleText)
        val essenceBlock = mutableMapOf<String, String>("name" to "md",
            "text" to contentText)

        note = NoteModel(
            content = mutableListOf<MutableMap<String, String>>(infoBlock, essenceBlock),
            hashCode = null
        )

        saveNoteUseCase.execute(note)
        note.hashCode = note.hashCode()
        addHashUseCase.execute(note.hashCode!!)
    }

    fun getNote(hashCode: Int) {
        viewModelScope.launch {
            note = getNotesUseCase.execute(listOf(hashCode)).first()
        }
        note.content.forEach{
            if (it["name"] == "info") {
                titleText = it["header"].toString()
            }
        }
    }

    fun updateTitleText(newTitle: String) {
        note.content[0]["header"] = newTitle
    }

    fun updateMdText(index: Int, newText: String) {
        note.content[index]["text"] = newText
    }

    fun saveChanges() {
        note.hashCode?.let {
            changeNoteUseCase.execute(it, note)
        } ?: {
            Log.d("OpenNoteVM", "The changes is not saved, hashcode is null")
        }
    }
}