package com.luckhost.lockscreen_notes.presentation.screens.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private val vm by viewModel<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lockscreen_notesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.main_bg)
                ) {
                    ProfileScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileScreen() {
        val context = LocalContext.current
        val isLoading by vm.isLoading.collectAsState()
        val errorText by vm.errorText.collectAsState()
        val successText by vm.successText.collectAsState()
        val isLoggedOut by vm.isLoggedOut.collectAsState()

        val username by vm.username.collectAsState()
        val firstName by vm.firstName.collectAsState()
        val lastName by vm.lastName.collectAsState()
        val email by vm.email.collectAsState()

        val changePasswordOld by vm.changePasswordOld.collectAsState()
        val changePasswordNew by vm.changePasswordNew.collectAsState()
        val changePasswordConfirm by vm.changePasswordConfirm.collectAsState()

        var isEditingProfile by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }

        if (isLoggedOut) {
            LaunchedEffect(Unit) { finish() }
        }

        if (errorText.isNotEmpty()) {
            LaunchedEffect(errorText) {
                Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
                vm.clearMessages()
            }
        }

        if (successText.isNotEmpty()) {
            LaunchedEffect(successText) {
                Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
                vm.clearMessages()
                isEditingProfile = false
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Удалить аккаунт?", color = colorResource(R.color.main_title_text)) },
                text = {
                    Text(
                        "Это действие необратимо. Все ваши данные будут удалены.",
                        color = colorResource(R.color.main_title_text)
                    )
                },
                containerColor = colorResource(R.color.black_and_brown),
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            vm.deleteAccount()
                        },
                        colors = ButtonColors(
                            containerColor = Color.Red.copy(alpha = 0.8f),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Red.copy(alpha = 0.4f),
                            disabledContentColor = Color.White,
                        )
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Отмена", color = colorResource(R.color.grey_neutral))
                    }
                }
            )
        }

        Scaffold(
            containerColor = colorResource(R.color.main_bg),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Профиль",
                            style = TextStyle(
                                color = colorResource(R.color.main_title_text),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = colorResource(R.color.grey_neutral)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.main_bg)
                    )
                )
            }
        ) { innerPadding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(R.color.grey_neutral))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(16.dp))

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = colorResource(R.color.grey_neutral)
                    )

                    Spacer(Modifier.height(24.dp))

                    SectionTitle("Личные данные")
                    Spacer(Modifier.height(12.dp))

                    ProfileField(
                        label = "Имя пользователя",
                        value = username,
                        enabled = isEditingProfile,
                        onValueChange = vm::updateUsername,
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileField(
                        label = "Имя",
                        value = firstName,
                        enabled = isEditingProfile,
                        onValueChange = vm::updateFirstName,
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileField(
                        label = "Фамилия",
                        value = lastName,
                        enabled = isEditingProfile,
                        onValueChange = vm::updateLastName,
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileField(
                        label = "Email",
                        value = email,
                        enabled = isEditingProfile,
                        onValueChange = vm::updateEmail,
                        keyboardType = KeyboardType.Email,
                    )

                    Spacer(Modifier.height(16.dp))

                    if (!isEditingProfile) {
                        OutlineActionButton(
                            text = "Редактировать профиль",
                            onClick = { isEditingProfile = true },
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(2.dp, colorResource(R.color.grey_neutral)),
                                colors = ButtonColors(
                                    containerColor = colorResource(R.color.grey_neutral),
                                    contentColor = colorResource(R.color.heavy_metal),
                                    disabledContainerColor = colorResource(R.color.grey_neutral),
                                    disabledContentColor = colorResource(R.color.heavy_metal),
                                ),
                                onClick = { vm.saveProfile() }
                            ) {
                                Text("Сохранить", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(2.dp, colorResource(R.color.grey_neutral)),
                                colors = ButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = colorResource(R.color.grey_neutral),
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = colorResource(R.color.grey_neutral),
                                ),
                                onClick = {
                                    isEditingProfile = false
                                    vm.loadProfile()
                                }
                            ) {
                                Text("Отмена")
                            }
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(24.dp))

                    // Change password section — stub, requires backend endpoint
                    SectionTitle("Смена пароля")
                    Text(
                        text = "⚠ Функция будет доступна после реализации эндпоинта на сервере",
                        style = TextStyle(
                            color = colorResource(R.color.grey_neutral),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.SansSerif,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileField(
                        label = "Текущий пароль",
                        value = changePasswordOld,
                        enabled = true,
                        onValueChange = vm::updateChangePasswordOld,
                        isPassword = true,
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileField(
                        label = "Новый пароль",
                        value = changePasswordNew,
                        enabled = true,
                        onValueChange = vm::updateChangePasswordNew,
                        isPassword = true,
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileField(
                        label = "Подтвердите новый пароль",
                        value = changePasswordConfirm,
                        enabled = true,
                        onValueChange = vm::updateChangePasswordConfirm,
                        isPassword = true,
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlineActionButton(
                        text = "Изменить пароль",
                        onClick = { vm.changePassword() },
                    )

                    Spacer(Modifier.height(28.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(24.dp))

                    // Logout
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, Color.Red.copy(alpha = 0.6f)),
                        colors = ButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Red,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Red,
                        ),
                        onClick = { vm.logout() }
                    ) {
                        Text(
                            "Выйти из аккаунта",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Delete account section — stub, requires backend endpoint
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, Color.Red.copy(alpha = 0.3f)),
                        colors = ButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Red.copy(alpha = 0.6f),
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Red.copy(alpha = 0.4f),
                        ),
                        onClick = { showDeleteDialog = true }
                    ) {
                        Text(
                            "Удалить аккаунт",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }

    @Composable
    private fun SectionTitle(text: String) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = colorResource(R.color.main_title_text),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
            )
        )
    }

    @Composable
    private fun ProfileField(
        label: String,
        value: String,
        enabled: Boolean,
        onValueChange: (String) -> Unit,
        isPassword: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    label,
                    color = colorResource(R.color.grey_neutral)
                )
            },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else
                androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(
                color = colorResource(R.color.main_title_text),
                fontSize = 16.sp,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.grey_neutral),
                unfocusedBorderColor = colorResource(R.color.grey_neutral).copy(alpha = 0.5f),
                disabledBorderColor = colorResource(R.color.grey_neutral).copy(alpha = 0.3f),
                disabledTextColor = colorResource(R.color.main_title_text).copy(alpha = 0.7f),
            ),
        )
    }

    @Composable
    private fun OutlineActionButton(text: String, onClick: () -> Unit) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, colorResource(R.color.grey_neutral)),
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = colorResource(R.color.grey_neutral),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = colorResource(R.color.grey_neutral),
            ),
            onClick = onClick,
        ) {
            Text(text, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        }
    }
}
