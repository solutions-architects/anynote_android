package com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.userLogin.LoginViewModel

@Composable
fun SignUpLayout(
    vm: LoginViewModel
) {
    val loginTextState by vm.loginTextState.collectAsState()
    val passwordTextState by vm.passwordTextState.collectAsState()
    val passwordRepeatTextState by vm.passwordRepeatTextState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout {
            val (headerRef, nicknameInpRef,
                passInpRef, buttonsRowRef, repeatPusRef) = createRefs()
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(headerRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(nicknameInpRef.top, margin = 25.dp)
                    }
                    .fillMaxWidth(),
                text = stringResource(id = R.string.login_activity_sign_up_text),
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
                labelText = stringResource(id = R.string.login_activity_nickname_text_field),
                value = loginTextState,
                onValueChange = { text -> vm.updateLoginText(text) }
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
                onValueChange = { text -> vm.updatePasswordRepeatText(text) }
            )

            LoginInputField(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(repeatPusRef) {
                        top.linkTo(passInpRef.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                labelText =
                stringResource(id = R.string.login_activity_repeat_password_text_field),
                value = passwordRepeatTextState,
                onValueChange = { text -> vm.updatePasswordText(text) }
            )

            Row(modifier = Modifier
                .constrainAs(buttonsRowRef) {
                    top.linkTo(repeatPusRef.bottom, margin = 24.dp)
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
                    onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.login_activity_sign_in_button),
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
                    onClick = {
                        vm.getToken()
                    }) {
                    Text(text = stringResource(id = R.string.login_activity_next_button),
                        style = TextStyle(fontSize = 26.sp),
                        color = colorResource(id = R.color.heavy_metal),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}