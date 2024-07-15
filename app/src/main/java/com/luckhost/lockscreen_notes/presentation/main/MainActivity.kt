package com.luckhost.lockscreen_notes.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.luckhost.domain.models.NoteModel
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.createNote.OpenNoteActivity
import com.luckhost.lockscreen_notes.presentation.ui.NoteBox
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainViewModel>()
    private val notesList = mutableStateListOf<NoteModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lockscreen_notesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.main_bg)
                ) {
                    Column {
                        val notesListState = remember { notesList }
                        NotesList(
                            notes = notesListState,
                            onDeleteButClick = {
                                model: NoteModel ->  model.hashCode?.let {
                                    vm.deleteNote(it)
                                    notesListState.remove(model)}
                            }
                        )

                        Button(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(20.dp),
                            onClick = {
                                notesListState.add(vm.createNote(
                                    header = "aboba",
                                    content = "or no aboba",
                                    coordinateX = 0,
                                    coordinateY = 0,
                                    deadLine = Date()
                                ))
                            }
                        ) {}
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notesList.clear()
        val notes = vm.getNotes()
        notesList.addAll(notes)
        notes.forEach{
            Log.d("onMainResume", it.header)
        }

    }
    @Composable
    fun NotesList(notes: SnapshotStateList<NoteModel>,
                  onDeleteButClick: (NoteModel) -> Unit ) {
        val context = LocalContext.current
        LazyColumn(modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            items(notes){
                    item ->
                NoteBox(
                    title = item.header,
                    content = item.content,
                    bitmap = null,
                    onItemClick = {
                        val intent = Intent(context, OpenNoteActivity::class.java)
                        intent.putExtra("noteHash", item.hashCode)
                        context.startActivity(intent) },
                    onDeleteIconClick = { onDeleteButClick(item) }
                )
            }
        }
    }
}



