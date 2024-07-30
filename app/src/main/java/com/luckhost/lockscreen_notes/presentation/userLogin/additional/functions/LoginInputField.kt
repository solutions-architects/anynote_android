package com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R

@Composable
fun LoginInputField(
    modifier: Modifier,
    labelText: String,
) {
    var nicknameFieldState by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = nicknameFieldState,
        onValueChange = { nicknameFieldState = it },
        label = {
            Text(
                text = labelText,
                color = colorResource(id = R.color.grey_neutral)
            )
        },
        textStyle = TextStyle(
            color = colorResource(id = R.color.grey_neutral),
            fontSize = 16.sp
        ),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = colorResource(id = R.color.grey_neutral),
            focusedBorderColor = colorResource(id = R.color.grey_neutral),
            unfocusedBorderColor = colorResource(id = R.color.stroke_color),
            focusedLabelColor = colorResource(id = R.color.grey_neutral),
            unfocusedLabelColor = colorResource(id = R.color.grey_neutral),
            focusedPlaceholderColor = colorResource(id = R.color.grey_neutral),
        )
    )
}