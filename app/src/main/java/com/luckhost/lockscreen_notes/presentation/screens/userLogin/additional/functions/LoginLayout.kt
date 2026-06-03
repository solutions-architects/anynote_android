package com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional.functions

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.LoginViewModel

@Composable
fun LoginLayout(
    vm: LoginViewModel,
    onApplyButClick: () -> Unit,
    onSignUpButClick: () -> Unit,
    onBackHandler: () -> Unit,
    onLogoutClick: () -> Unit,
    onConnectGithubClick: (Context) -> Unit,
    onDisconnectGithubClick: () -> Unit,
) {
    val loginTextState by vm.loginTextState.collectAsState()
    val passwordTextState by vm.passwordTextState.collectAsState()
    val errorText by vm.errorTextState.collectAsState()
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val githubUsername by vm.githubUsername.collectAsState()

    BackHandler {
        onBackHandler()
    }

    if (isLoggedIn) {
        AccountLayout(
            githubUsername = githubUsername,
            onLogoutClick = onLogoutClick,
            onConnectGithubClick = onConnectGithubClick,
            onDisconnectGithubClick = onDisconnectGithubClick,
        )
    } else {
        LoginFormLayout(
            loginTextState = loginTextState,
            passwordTextState = passwordTextState,
            errorText = errorText,
            onLoginChange = { vm.updateLoginText(it) },
            onPasswordChange = { vm.updatePasswordText(it) },
            onApplyButClick = onApplyButClick,
            onSignUpButClick = onSignUpButClick,
        )
    }
}

@Composable
private fun AccountLayout(
    githubUsername: String?,
    onLogoutClick: () -> Unit,
    onConnectGithubClick: (Context) -> Unit,
    onDisconnectGithubClick: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = colorResource(id = R.color.grey_neutral)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.login_activity_account_title),
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(24.dp))

            if (githubUsername != null) {
                Text(
                    text = "GitHub: @$githubUsername",
                    style = TextStyle(
                        color = colorResource(id = R.color.main_title_text),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, colorResource(id = R.color.grey_neutral)),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colorResource(id = R.color.grey_neutral),
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = colorResource(id = R.color.grey_neutral),
                    ),
                    onClick = onDisconnectGithubClick
                ) {
                    Text(
                        text = "Отключить GitHub",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            } else {
                Button(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, colorResource(id = R.color.grey_neutral)),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = colorResource(id = R.color.grey_neutral),
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = colorResource(id = R.color.grey_neutral),
                    ),
                    onClick = { onConnectGithubClick(context) }
                ) {
                    Text(
                        text = "Подключить GitHub",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Red.copy(alpha = 0.6f)),
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Red,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Red,
                ),
                onClick = onLogoutClick
            ) {
                Text(
                    text = stringResource(id = R.string.login_activity_logout_button),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }
    }
}

@Composable
private fun LoginFormLayout(
    loginTextState: String,
    passwordTextState: String,
    errorText: String,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onApplyButClick: () -> Unit,
    onSignUpButClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout {
            val (errTextRef, headerRef, nicknameInpRef,
                passInpRef, buttonsRowRef) = createRefs()

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 45.dp)
                    .constrainAs(errTextRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(headerRef.top, margin = 25.dp)
                    },
                text = errorText,
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                ),
            )

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(headerRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(nicknameInpRef.top, margin = 25.dp)
                    }
                    .fillMaxWidth(),
                text = stringResource(id = R.string.login_activity_main_text),
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                ),
            )

            LoginInputField(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(nicknameInpRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(passInpRef.top, margin = 24.dp)
                    },
                labelText = stringResource(id = R.string.login_activity_email_text_field),
                value = loginTextState,
                onValueChange = onLoginChange,
            )

            LoginInputField(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(passInpRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                labelText = stringResource(id = R.string.login_activity_password_text_field),
                value = passwordTextState,
                onValueChange = onPasswordChange,
                isPassword = true,
            )

            Row(
                modifier = Modifier
                    .constrainAs(buttonsRowRef) {
                        top.linkTo(passInpRef.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .wrapContentSize(),
                    onClick = onSignUpButClick,
                ) {
                    Text(
                        text = stringResource(id = R.string.login_activity_sign_in_button),
                        color = colorResource(id = R.color.main_title_text),
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }
                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .width(160.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, colorResource(id = R.color.stroke_color)),
                    colors = ButtonColors(
                        containerColor = colorResource(id = R.color.grey_neutral),
                        contentColor = colorResource(id = R.color.light_grey),
                        disabledContainerColor = colorResource(id = R.color.grey_neutral),
                        disabledContentColor = colorResource(id = R.color.light_grey),
                    ),
                    onClick = onApplyButClick,
                ) {
                    Text(
                        text = stringResource(id = R.string.login_activity_next_button),
                        style = TextStyle(fontSize = 26.sp),
                        color = colorResource(id = R.color.heavy_metal),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
