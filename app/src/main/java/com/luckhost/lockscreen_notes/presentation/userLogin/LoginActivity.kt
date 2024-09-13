package com.luckhost.lockscreen_notes.presentation.userLogin

import android.app.Activity
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
import com.luckhost.lockscreen_notes.presentation.userLogin.additional.Destination
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
                val activity = LocalContext.current as Activity

                NavHost(navController = navController, startDestination =
                Destination.Login.route) {
                    composable(Destination.Login.route) {
                        LoginLayout(
                            vm = vm,
                            onApplyButClick = {
                                vm.clearErrorText()
                                vm.getToken()
                                navController.navigate(
                                    Destination.Loading.createRoute(Destination.Login.route))
                            },
                            onSignUpButClick = {
                                vm.clearErrorText()
                                navController.navigate(
                                    Destination.SignUp.route
                                )
                            },
                            onBackHandler = {
                                vm.clearErrorText()
                                activity.finish()
                            }
                        )
                    }

                    composable(Destination.SignUp.route) {
                        SignUpLayout(
                            vm = vm,
                            onApplyButClick = {
                                vm.clearErrorText()
                                vm.signUp()
                                navController.navigate(
                                    Destination.Loading.createRoute(Destination.SignUp.route))
                            },
                            onBackHandlerClick = {
                                vm.clearErrorText()
                                navController.navigate(
                                    Destination.Login.route
                                )
                            }
                        )
                    }

                    composable(Destination.Loading.route) { backStackEntry ->
                        val from = backStackEntry.arguments?.getString("from")
                        LoadingLayout(
                            vm = vm,
                            onLoadingEnd = {
                                navController.clearBackStack(Destination.Loading.route)
                                if (from != null) {
                                    navController.navigate(from)
                                } else {
                                    navController.navigate(Destination.Login.route)
                                }
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
