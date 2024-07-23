package com.luckhost.lockscreen_notes.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.luckhost.lockscreen_notes.presentation.main.additional.functions.NoteBox
import com.luckhost.lockscreen_notes.presentation.userLogin.LoginActivity
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
                ScreenLayout()
            }
        }
    }

    @Composable
    fun ScreenLayout() {
        val notesListState = remember { notesList }
        val context = LocalContext.current

        Scaffold(
            topBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        modifier = Modifier
                            .padding(start = 8.dp,
                                top = 8.dp),
                        onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Menu,
                            modifier = Modifier.size(90.dp),
                            contentDescription = "Profile",
                            tint = colorResource(id = R.color.grey_neutral)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 8.dp,
                                top = 8.dp),
                        onClick = {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        }) {
                        Icon(Icons.Default.AccountCircle,
                            modifier = Modifier.size(90.dp),
                            contentDescription = "Profile",
                            tint = colorResource(id = R.color.grey_neutral)
                        )
                    }
                }
            },
            containerColor = colorResource(id = R.color.main_bg),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    startOpenNoteActivity(context)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) {
            innerPadding  ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                Column(modifier = Modifier.padding(top = 15.dp)) {
                    HorizontalDivider()
                    NotesList(
                        notes = notesListState,
                        onDeleteButClick = {
                                model: NoteModel ->  model.hashCode?.let {
                            vm.deleteNote(it)
                            notesListState.remove(model)}
                        }
                    )
                }

            }
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
                        startOpenNoteActivity(context = context, item)
                    },
                    onDeleteIconClick = { onDeleteButClick(item) }
                )
            }
        }
    }

    private fun startOpenNoteActivity(context: Context, note: NoteModel) {
        val intent = Intent(context, OpenNoteActivity::class.java)
        intent.putExtra("noteHash", note.hashCode)
        context.startActivity(intent)
    }

    private fun startOpenNoteActivity(context: Context) {
        val intent = Intent(context, OpenNoteActivity::class.java)
        context.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        notesList.clear()
        val notes = vm.getNotes()
        notesList.addAll(notes)
    }
}



