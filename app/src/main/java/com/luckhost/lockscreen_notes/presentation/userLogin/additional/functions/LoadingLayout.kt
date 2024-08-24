package com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

import com.luckhost.lockscreen_notes.R

import com.luckhost.lockscreen_notes.presentation.userLogin.LoginViewModel

@Composable
fun LoadingLayout(
    vm: LoginViewModel,
    onLoadingEnd: () -> Unit,
    ) {

    /*TODO*/
    val isLoading = vm.isLoadingState.collectAsState()
    Log.d("LoadingLayout", isLoading.toString())


    LaunchedEffect(isLoading) {
        onLoadingEnd()
    }
    if (isLoading.value) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.login_activity_loading_text)
        )
    }
}