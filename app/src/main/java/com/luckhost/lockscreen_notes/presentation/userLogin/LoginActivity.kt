package com.luckhost.lockscreen_notes.presentation.userLogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Surface
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
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.Login
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.SignUp
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions.LoginInputField
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions.LoginLayout
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions.SignUpLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {
    private val vm by viewModel<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = R.color.main_bg)
            ) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Login) {
                    composable<Login> {
                        LoginLayout(
                            vm = vm,
                            onSignUpButClick = {
                                navController.navigate(SignUp)
                            }
                        )
                    }

                    composable<SignUp> {
                        SignUpLayout(
                            vm = vm
                        )
                    }
                }
            }
        }
    }
}
