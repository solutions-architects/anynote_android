package com.example.yourapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmDialog(
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Подтверждение") },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("Да")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Нет")
            }
        }
    )
}

@Preview
@Composable
fun PreviewConfirmDialog() {
    ConfirmDialog(
        text = "Вы уверены?",
        onConfirm = {},
        onDismiss = {}
    )
}
