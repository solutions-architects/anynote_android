package com.luckhost.lockscreen_notes.presentation.userLogin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.Loading
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.Login
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.SignUp
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.functions.LoadingLayout
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
                            onApplyButClick = {
                                vm.getToken()
                                navController.navigate(Loading)
                            },
                            onSignUpButClick = {
                                navController.navigate(SignUp)
                            }
                        )
                    }

                    composable<SignUp> {
                        SignUpLayout(
                            vm = vm,
                            onApplyButClick = {
                                vm.signUp()
                                navController.navigate(Loading)
                            },
                        )
                    }

                    composable<Loading> {
                        LoadingLayout(
                            vm = vm,
                            onLoadingEnd = {
                                navController.navigate(Login)
                                navController.clearBackStack<Loading>()
                            }
                        )
                    }
                }

                val toastNotification by vm.toastNotification.collectAsState()

                val context = LocalContext.current
                if (toastNotification.isNotEmpty()) {
                    LaunchedEffect(vm.toastNotification) {
                        Toast.makeText(context, toastNotification,
                            Toast.LENGTH_SHORT).show()
                        vm.clearToastNotification()
                    }
                }
            }
        }
    }
}
