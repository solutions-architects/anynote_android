package com.luckhost.lockscreen_notes.presentation.openNote.additional.functions

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
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

        val mainNotePart by vm.mainPartState.collectAsState()

        /* TODO change to lazy column */
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
    var textValue by remember {
        mutableStateOf(textFieldStates[index] ?: TextFieldValue("")) }

    val pasteText by vm.textPastState.collectAsState()

    LaunchedEffect(pasteText) {
        if (pasteText.isNotEmpty()) {
            val cursorPosition = textValue.selection.start
            val newText = textValue.text.substring(0, cursorPosition) +
                    "![image]($pasteText)" +
                    textValue.text.substring(cursorPosition)

            textValue = textValue.copy(
                text = newText,
                selection = TextRange(cursorPosition + "![image]($pasteText)".length)
            )
            vm.updateMdStateText(index, textValue.text)
        }
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = textValue,
        trailingIcon = {
            if (textValue.text.isNotEmpty()) {
                IconButton(
                    modifier = Modifier
                        ,
                    onClick = {
                        textValue = TextFieldValue("")
                        vm.updateTextFieldState(index, textValue)
                        vm.updateMdStateText(index, textValue.text)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }
        },
        onValueChange = { newText ->
            textValue = newText
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
