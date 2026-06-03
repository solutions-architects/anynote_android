package com.luckhost.lockscreen_notes.presentation.screens.userLogin

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional.Destination
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional.functions.EmailVerificationLayout
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional.functions.LoadingLayout
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional.functions.LoginLayout
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional.functions.SignUpLayout
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val vm by viewModel<LoginViewModel>()

    override fun onResume() {
        super.onResume()
        vm.refreshGithubState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lockscreen_notesTheme {
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
                                },
                                onLogoutClick = {
                                    vm.logout()
                                },
                                onConnectGithubClick = { context ->
                                    vm.connectGithub(context)
                                },
                                onDisconnectGithubClick = {
                                    vm.disconnectGithub()
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

                        composable(Destination.EmailVerification.route) {
                            EmailVerificationLayout(vm = vm)
                        }

                        composable(Destination.EmailVerifying.route) {
                            LoadingLayout(
                                vm = vm,
                                onLoadingEnd = {
                                    val errorText = vm.errorTextState.value
                                    if (errorText.isEmpty()) {
                                        navController.navigate(Destination.Login.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Destination.EmailVerification.route) {
                                            popUpTo(Destination.EmailVerifying.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable(Destination.Loading.route) { backStackEntry ->
                            val from = backStackEntry.arguments?.getString("from")
                            LoadingLayout(
                                vm = vm,
                                onLoadingEnd = {
                                    if (from == Destination.SignUp.route
                                        && vm.signUpSuccessState.value) {
                                        navController.navigate(
                                            Destination.EmailVerification.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else {
                                        navController.clearBackStack(Destination.Loading.route)
                                        if (from != null) {
                                            navController.navigate(from)
                                        } else {
                                            navController.navigate(Destination.Login.route)
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Handle email verification deep link
                    LaunchedEffect(Unit) {
                        val uri = intent.data
                        if (uri != null) {
                            val token = uri.getQueryParameter("token")
                            if (token != null) {
                                vm.clearErrorText()
                                vm.setPendingVerifyToken(token)
                                vm.startEmailVerification()
                                navController.navigate(Destination.EmailVerifying.route)
                            }
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
}
