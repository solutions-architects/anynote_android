package com.luckhost.lockscreen_notes.presentation.openNote.additional.functions

import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteViewModel
import com.luckhost.lockscreen_notes.presentation.ui.ConfirmDialog

@Composable
fun EditNoteFragment(vm: OpenNoteViewModel) {
    val showDialog by vm.showDialog.collectAsState()

    if (showDialog) {
        ConfirmDialog(
            text = "Save changes?",
            onConfirm = { vm.saveChanges()
                vm.changeEditModeState()
                vm.hideDialogState() },
            onDismiss = {
                vm.changeEditModeState()
                vm.returnOldValues()
                vm.hideDialogState() },
            onBackHandler = {
                vm.hideDialogState()
            }
        )
    }

    BackHandler {
        vm.showDialogState()
    }
    
    Column {
        val titleTextState by vm.titleTextState.collectAsState()

        val maxLength = 20
        TextField(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(),
            value = titleTextState,
            onValueChange = {
                if (it.length <= maxLength){
                    vm.updateTitleStateText(it)
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
                    IconButton(onClick = { vm.updateTitleStateText("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        val scrollState = rememberScrollState()

        val mainNotePart by remember { derivedStateOf { vm.mainPartState.value } }

        var isColumnVisible by remember{mutableStateOf(false)}

        /* TODO change to lazy column
        *   move to VM */
        LaunchedEffect(mainNotePart) {
            isColumnVisible = true
        }
        AnimatedVisibility(visible = isColumnVisible) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(scrollState)
            ) {
                mainNotePart.forEachIndexed { index, map ->
                    when (map["name"]) {
                        "md" -> TextPart(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .fillMaxWidth(),
                            vm = vm,
                            index = index,
                        )
                        "image" -> {
                            map["mediaLink"]?.let {
                                ImagePart(
                                    modifier = Modifier
                                        .weight(1f, fill = false)
                                        .fillMaxWidth(),
                                    vm = vm,
                                    mediaLink = it,
                                    index = index
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TextPart(
    modifier: Modifier,
    vm: OpenNoteViewModel,
    index: Int,
) {
    val textFieldStates by vm.textFieldStates.collectAsState()
    val mediaGetResult by vm.mediaGetResult.collectAsState()

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(mediaGetResult) {
        if (mediaGetResult.isNotEmpty() && isFocused) {
            textFieldStates[index]?.let {
                val cursorPosition = it.selection.start
                val beforeCursorText = it.text.substring(0, cursorPosition)
                val afterCursorText = it.text.substring(cursorPosition)
                vm.splitTextFieldWithImage(index, beforeCursorText, afterCursorText)
                vm.changeMediaGetResult("")
            }
        }
    }

    textFieldStates[index]?.let {
        TextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused }
            .fillMaxWidth(),
        interactionSource = interactionSource,
        value = it,
        trailingIcon = {
            if (it.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        vm.updateTextFieldState(index, TextFieldValue(""))
                        vm.updateMdStateText(index, it.text)
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }
        },
        onValueChange = { newText ->

            vm.updateTextFieldState(index, newText)
            vm.updateMdStateText(index, newText.text)
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
}

@Composable
fun ImagePart(
    modifier: Modifier,
    vm: OpenNoteViewModel,
    mediaLink: String,
    index: Int
) {
    val bitmap = BitmapFactory.decodeFile(mediaLink)
    Log.d("OpenNoteView", mediaLink)

    bitmap?.let {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit,
        )
    }
}
