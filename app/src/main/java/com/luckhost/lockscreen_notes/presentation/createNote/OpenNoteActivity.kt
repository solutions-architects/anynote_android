package com.luckhost.lockscreen_notes.presentation.createNote

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.luckhost.domain.models.NoteModel
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.createNote.additional.functions.EditNoteFragment
import com.luckhost.lockscreen_notes.presentation.createNote.additional.functions.NoteViewFragment
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenNoteActivity : ComponentActivity() {
    private val vm by viewModel<OpenNoteViewModel>()
    private lateinit var note: NoteModel
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        note = vm.getNote(intent.getIntExtra("noteHash", 0))

        setContent {
            Lockscreen_notesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.main_bg)
                ) {
                    Column {
                        var isEditMode by remember { mutableStateOf(false) }

                        Switch(
                            checked = isEditMode,
                            onCheckedChange = { isEditMode = it }
                        )
                        
                        if(isEditMode) {
                            EditNoteFragment(note = note,
                                onSaveClick = {hash: Int, note: NoteModel ->
                                    vm.changeNote(hash, note)})
                        } else {
                            NoteViewFragment(note = note)
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}