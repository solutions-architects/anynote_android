package com.luckhost.lockscreen_notes.presentation.openNote.additional.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.openNote.OpenNoteViewModel

/**
 * Text toolbar to change text state/apply changes/add new media
 */
@Composable
fun TextToolbar(
    modifier: Modifier,
    vm: OpenNoteViewModel
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let {
            val realPath = vm.getRealPathFromUri(it)
            vm.changeMediaGetResult(realPath)
        }
    }
    val focusedFieldIndex by vm.focusedTextFieldId.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(colorResource(id = R.color.heavy_metal)),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Button to add new media
        IconButton(onClick = { photoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        ) }) {
            Icon(
                painter = painterResource(id = R.drawable.attach_file),
                contentDescription = "Bold",
                tint = colorResource(id = R.color.grey_neutral)
            )
        }
        // Button to clear current field
        IconButton(onClick = {
            vm.updateMdStateText(
                focusedFieldIndex,
                ""
            )
            vm.updateTextFieldState(
                focusedFieldIndex,
                TextFieldValue("")
            )
        }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear text",
                tint = colorResource(id = R.color.grey_neutral)
            )
        }
        // Button to save changes field
        IconButton(onClick = {
            vm.saveChanges()
            vm.changeEditModeState()
        }) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Save",
                tint = colorResource(id = R.color.grey_neutral)
            )
        }
    }
}