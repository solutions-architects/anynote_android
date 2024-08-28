package com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.luckhost.lockscreen_notes.R

import com.luckhost.lockscreen_notes.presentation.userLogin.LoginViewModel

@Composable
fun LoadingLayout(
    vm: LoginViewModel,
    onLoadingEnd: () -> Unit,
    ) {

    val isLoading = vm.isLoadingState.collectAsState()
    Log.d("LoadingLayout", isLoading.toString())

    Box(modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isLoading.value) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp),
                text = stringResource(id = R.string.login_activity_loading_text),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                ),
            )
        } else {
            LaunchedEffect(Unit) {
                onLoadingEnd()
                Log.d("LoadingLayout exit", isLoading.toString())
            }
        }
    }
}