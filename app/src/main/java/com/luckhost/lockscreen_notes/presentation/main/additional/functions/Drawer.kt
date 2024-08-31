package com.luckhost.lockscreen_notes.presentation.main.additional.functions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text(text = "Саня Миша Егор и Антон")
    }
}

@Composable
fun DrawerBody() {
    Text(text = "программистичи имбаоаоаоао")
}