package com.luckhost.lockscreen_notes.presentation.createNote

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.luckhost.domain.models.NoteModel
import com.luckhost.lockscreen_notes.R
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
                        var textState by remember { mutableStateOf(note.header) }
                        val maxLength = 20
                        TextField(
                            modifier = Modifier
                                .wrapContentSize()
                                .fillMaxWidth(),
                            value = textState,
                            onValueChange = {
                                if (it.length <= maxLength) textState = it
                            },
                            textStyle = TextStyle(
                                color = colorResource(id = R.color.notebox_title_text),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(id = R.color.main_bg),
                                unfocusedContainerColor = colorResource(id = R.color.main_bg),
                            ),
                            trailingIcon = {
                                if (textState.isNotEmpty()) {
                                    IconButton(onClick = { textState = "" }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                        TextField(modifier = Modifier
                            .wrapContentSize()
                            .fillMaxWidth(),
                            value = note.content,
                            onValueChange = {},
                            textStyle = TextStyle(
                                color = colorResource(id = R.color.grey_neutral),
                                fontSize = 16.sp
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(id = R.color.main_bg),
                                unfocusedContainerColor = colorResource(id = R.color.main_bg),
                            ),
                        )
                        Button(modifier = Modifier.wrapContentSize(),
                            onClick = {
                                note.header = textState
                                note.hashCode?.let { vm.changeNote(it, note) }
                            }) {
                            Text(modifier = Modifier.wrapContentSize(),
                                text = "Save")
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