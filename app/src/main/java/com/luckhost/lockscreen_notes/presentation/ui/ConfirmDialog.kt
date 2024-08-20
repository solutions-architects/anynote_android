package com.luckhost.lockscreen_notes.presentation.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luckhost.lockscreen_notes.R

@Composable
fun ConfirmDialog(
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.confirm_dialog_question)) },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(stringResource(id = R.string.confirm_dialog_answer_yes))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.confirm_dialog_answer_no))
            }
        }
    )
}

@Preview
@Composable
fun PreviewConfirmDialog() {
    ConfirmDialog(
        stringResource(id = R.string.confirm_dialog_question),
        onConfirm = {},
        onDismiss = {}
    )
}
