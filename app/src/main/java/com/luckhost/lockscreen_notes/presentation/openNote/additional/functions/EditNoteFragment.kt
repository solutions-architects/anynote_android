package com.luckhost.lockscreen_notes.presentation.openNote.additional.functions

import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
            onConfirm = {
                vm.saveChanges()
                vm.changeEditModeState()
                vm.hideDialogState()
            },
            onDismiss = {
                vm.changeEditModeState()
                vm.returnOldValues()
                vm.hideDialogState()
            },
            onBackHandler = {
                vm.hideDialogState()
            }
        )
    }

    BackHandler {
        vm.showDialogState()
    }

    // Используем Box для наложения элементов и фиксированной панели инструментов внизу
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp) // Оставляем место для панели инструментов
        ) {
            val titleTextState by vm.titleTextState.collectAsState()

            val maxLength = 20
            TextField(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                value = titleTextState,
                onValueChange = {
                    if (it.length <= maxLength) {
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


            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Делаем LazyColumn гибким и занимающим оставшееся место
                    .fillMaxSize(),
            ) {
                itemsIndexed(vm.mainPartState) { index, map ->
                    when (map["name"]) {
                        "md" -> TextPart(
                            modifier = Modifier
                                .fillMaxWidth(),
                            vm = vm,
                            index = index,
                        )
                        "image" -> {
                            map["mediaLink"]?.let {
                                ImagePart(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    mediaLink = it,
                                )
                            }
                        }
                    }
                }
            }
        }

        // Фиксируем панель инструментов внизу
        TextToolbar(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Панель всегда будет внизу
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(Color.Gray),
            vm = vm
        )
    }
}

@Composable
fun TextToolbar(
    modifier: Modifier,
    vm: OpenNoteViewModel
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            val realPath = vm.getRealPathFromUri(it)
            vm.changeMediaGetResult(realPath)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.heavy_metal)),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = { photoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        ) }) {
            Icon(
                painter = painterResource(id = R.drawable.attach_file),
                contentDescription = "Bold",
                tint = colorResource(id = R.color.grey_neutral)
            )
        }
        IconButton(onClick = { /* Действие кнопки 2 */ }) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Italic",
                tint = colorResource(id = R.color.grey_neutral)
            )
        }
        IconButton(onClick = { /* Действие кнопки 3 */ }) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Underline",
                tint = colorResource(id = R.color.grey_neutral)
            )
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

    LaunchedEffect(mediaGetResult) {
        if (mediaGetResult != "" && isFocused) {
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
                    isFocused = focusState.isFocused
                }
                .fillMaxWidth(),
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
                focusedIndicatorColor = Color.Transparent
            ),
        )
    }
}

@Composable
fun ImagePart(
    modifier: Modifier,
    mediaLink: String,
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
