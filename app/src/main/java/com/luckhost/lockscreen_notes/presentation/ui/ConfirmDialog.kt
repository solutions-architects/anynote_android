package com.luckhost.lockscreen_notes.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luckhost.lockscreen_notes.R

@Composable
fun ConfirmDialog(
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onBackHandler: () -> Unit = { }
) {
    AlertDialog(
        onDismissRequest = { onBackHandler() },
        title = { Text(text = stringResource(id = R.string.confirm_dialog_question)) },
        text = { Text(text) },
        containerColor = colorResource(id = R.color.light_grey),
        titleContentColor = colorResource(id = R.color.main_title_text),
        textContentColor = colorResource(id = R.color.main_title_text),
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.grey_neutral),
                    contentColor = colorResource(id = R.color.heavy_metal),
                ),
                border = BorderStroke(
                    width = 3.dp,
                    color = colorResource(id = R.color.grey_neutral)
                )
            ) {
                Text(stringResource(id = R.string.confirm_dialog_answer_yes))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_grey),
                    contentColor = colorResource(id = R.color.grey_neutral),
                ),
                border = BorderStroke(
                    width = 3.dp,
                    color = colorResource(id = R.color.grey_neutral)
                )
                ) {
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
