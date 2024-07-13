package com.luckhost.lockscreen_notes.presentation.createNote

import androidx.lifecycle.ViewModel
import com.luckhost.domain.useCases.keys.SaveHashesUseCase
import com.luckhost.domain.useCases.objects.SaveNoteUseCase

class CreateNoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val saveHashesUseCase: SaveHashesUseCase,
) : ViewModel() {
}