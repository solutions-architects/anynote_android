package com.luckhost.lockscreen_notes.presentation.createNote

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
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
import java.util.Date

class OpenNoteActivity : ComponentActivity() {
    private val vm by viewModel<OpenNoteViewModel>()
    private lateinit var note: NoteModel
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        
        extras?.let {
            val noteHash = intent.getIntExtra("noteHash", 0)
            note = vm.getNote(noteHash)
        }?: run {
            note = vm.createNote(
                header = "Empty note",
                content = "tap to edit",
                coordinateX = 0,
                coordinateY = 0,
                deadLine = Date()
            )
        }

        setContent {
            Lockscreen_notesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = colorResource(id = R.color.main_bg),
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }) {

                        }
                    }
                ) {
                    innerPadding  ->
                    Column(modifier = Modifier.padding(innerPadding)) {
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
}