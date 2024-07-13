package com.luckhost.lockscreen_notes.presentation.main

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.luckhost.domain.models.NoteModel
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.ui.NoteBox
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lockscreen_notesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.main_bg)
                ) {
                    Column {

                        val listOfNotes = remember {
                            mutableStateListOf<NoteModel>()
                        }
                        listOfNotes.addAll(vm.getNotes())

                        NotesList(
                            notes = listOfNotes,
                            onItemClick = {
                                model: NoteModel ->  model.hashCode?.let {
                                    vm.deleteNote(it)
                                    listOfNotes.remove(model)}
                                Log.d("onClick", "execute/ ${model.hashCode}")
                            }
                        )

                        Button(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(20.dp),
                            onClick = {
                                listOfNotes.add(vm.createNote(
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
}

@Composable
fun NotesList(notes: SnapshotStateList<NoteModel>,
              onItemClick: (NoteModel) -> Unit ) {
    LazyColumn(modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        items(notes){
                item -> NoteBox(
            title = item.header,
            content = item.content,
            bitmap = null,
            onClick = { onItemClick(item) })
        }
    }
}

@Composable
fun NoteObj() {
    NoteBox(title = "Test",
        content = "lorem ipsum",
        bitmap = ImageBitmap.imageResource(R.drawable.aboba)) {
    }
}
