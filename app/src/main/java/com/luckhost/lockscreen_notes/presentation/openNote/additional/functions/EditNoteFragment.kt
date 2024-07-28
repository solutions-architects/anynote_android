package com.luckhost.lockscreen_notes.presentation.openNote.additional.functions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteActivity
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteViewModel

@Composable
fun EditNoteFragment(vm: OpenNoteViewModel) {
    Column {
        var titleTextState by remember { mutableStateOf(vm.titleText) }

        val maxLength = 20
        TextField(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(),
            value = titleTextState,
            onValueChange = {
                if (it.length <= maxLength){
                    titleTextState = it
                    vm.updateTitleText(titleTextState)
                }
            },
            textStyle = TextStyle(
                color = colorResource(id = R.color.main_title_text),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.main_bg),
                unfocusedContainerColor = colorResource(id = R.color.main_bg),
            ),
            trailingIcon = {
                if (titleTextState.isNotEmpty()) {
                    IconButton(onClick = { titleTextState = "" }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        vm.note.content.forEachIndexed { index, map ->
            when(map["name"]) {
                "md" -> TextPart(
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth(),
                    initialVale = map["text"].toString(),
                    onTextChange = { newText ->
                        vm.updateMdText(index, newText)
                    }
                )
            }
        }

        Button(modifier = Modifier.wrapContentSize(),
            onClick = {
                vm.saveChanges()
            }) {
            Text(modifier = Modifier.wrapContentSize(),
                text = "Save")
        }
    }
}

@Composable
private fun TextPart(
    modifier: Modifier,
    initialVale: String,
    onTextChange: (String) -> Unit
    ) {
    var contentTextState by remember { mutableStateOf(initialVale) }
    TextField(
        modifier = modifier,
        value = contentTextState,
        onValueChange = {
            contentTextState = it
            onTextChange(it)
        },
        textStyle = TextStyle(
            color = colorResource(id = R.color.grey_neutral),
            fontSize = 24.sp
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.main_bg),
            unfocusedContainerColor = colorResource(id = R.color.main_bg),
        ),
    )
}