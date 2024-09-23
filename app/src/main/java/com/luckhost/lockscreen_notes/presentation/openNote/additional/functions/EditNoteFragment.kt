package com.luckhost.lockscreen_notes.presentation.openNote.additional.functions

import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteViewModel
import com.luckhost.lockscreen_notes.presentation.ui.ConfirmDialog

@Composable
fun EditNoteFragment(vm: OpenNoteViewModel) {
    val showDialog by vm.showSaveChangesDialog.collectAsState()

    if (showDialog) {
        ConfirmDialog(
            text = stringResource(id = R.string.confirm_dialog_save_changes_question),
            onConfirm = {
                vm.saveChanges()
                vm.changeEditModeState()
                vm.hideSaveChangesDialogState()
            },
            onDismiss = {
                vm.changeEditModeState()
                vm.returnOldValues()
                vm.hideSaveChangesDialogState()
            },
            onBackHandler = {
                vm.hideSaveChangesDialogState()
            }
        )
    }

    BackHandler {
        vm.showSaveChangesDialogState()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp)
        ) {
            val titleTextState by vm.titleTextState.collectAsState()
            val lazyListState = rememberLazyListState()

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
                    .weight(1f)
                    .fillMaxSize(),
                state = lazyListState
            ) {
                itemsIndexed(vm.mainPartState) { index, map ->
                    when (map["name"]) {
                        "md" -> TextPart(
                            modifier = Modifier
                                .fillMaxWidth(),
                            vm = vm,
                            index = index,
                            lazyListState
                        )
                        "image" -> {
                            map["mediaLink"]?.let {
                                ImagePart(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    mediaLink = it,
                                    vm = vm,
                                    index = index
                                )
                            }
                        }
                    }
                }
            }
        }

        TextToolbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
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
            .height(40.dp)
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
        IconButton(onClick = {
            vm.showSaveChangesDialogState()
        }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = colorResource(id = R.color.grey_neutral)
            )
        }
        IconButton(onClick = {
            vm.saveChanges()
            vm.changeEditModeState()
        }) {
            Icon(
                Icons.Default.Check,
                contentDescription = "save",
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
    scrollState: LazyListState
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

    val cursorPosition = textFieldStates[index]?.selection?.start ?: 0
    LaunchedEffect(cursorPosition) {
        if (isFocused) {
            scrollState.animateScrollToItem(
                index = index,
                scrollOffset = 0
            )
            Log.d("EditNoteFragment", "$index $cursorPosition")
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
    vm: OpenNoteViewModel,
    index: Int
) {

    val showDialog by vm.showDeleteImageDialog.collectAsState()
    val bitmap = BitmapFactory.decodeFile(mediaLink)

    if (showDialog) {
        ConfirmDialog(
            text = stringResource(id = R.string.confirm_dialog_delete_image_question),
            onConfirm = {
                vm.deleteImageAndMergeText(index)
                vm.hideDeleteImageDialogState()
            },
            onDismiss = {
                vm.hideDeleteImageDialogState()
            },
            onBackHandler = {
                vm.hideDeleteImageDialogState()
            }
        )
    }

    Box(
        modifier =
            modifier
    ) {
        bitmap?.let {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .clickable {

                    }
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onClick = {
                vm.showDeleteImageDialogState()
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = colorResource(id = R.color.main_bg),
                contentColor = colorResource(id = R.color.grey_neutral),
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null
            )
        }
    }
}
