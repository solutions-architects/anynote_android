package com.luckhost.lockscreen_notes.presentation.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.main.additional.functions.DrawerBody
import com.luckhost.lockscreen_notes.presentation.main.additional.functions.DrawerHeader
import com.luckhost.lockscreen_notes.presentation.main.additional.functions.NoteBox
import com.luckhost.lockscreen_notes.presentation.userLogin.LoginActivity
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestPermissionAtStart()
            Lockscreen_notesTheme {
                ScreenLayout()
            }
        }
    }

    @Composable
    fun RequestPermissionAtStart() {
        val context = LocalContext.current
        var showDialog by remember { mutableStateOf(false) }
        var hasPermission by remember { mutableStateOf(false) }

        val activity = (context as? Activity)

        val permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                Manifest.permission.READ_MEDIA_IMAGES
            }
            else -> {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                hasPermission = true
                showDialog = false
            } else {
                showDialog = true
            }
        }

        LaunchedEffect(Unit) {
            permissionLauncher.launch(permission)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(stringResource(id =
                    R.string.main_activity_permission_denied_title)) },
                text = { Text(stringResource(id =
                    R.string.main_activity_image_read_permission_needed)) },
                containerColor = colorResource(id = R.color.black_and_brown),
                titleContentColor = colorResource(id = R.color.main_title_text),
                textContentColor = colorResource(id = R.color.main_title_text),
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            activity?.finish()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.black_and_brown),
                            contentColor = colorResource(id = R.color.grey_neutral),
                        ),
                        border = BorderStroke(
                            width = 3.dp,
                            color = colorResource(id = R.color.grey_neutral)
                        )
                    ) {
                        Text(text = stringResource(id =
                                R.string.main_activity_reject_permission_granting),
                            style = TextStyle(
                                fontSize = 14.sp,
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDialog = false
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package",
                                    context.packageName, null)
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.grey_neutral),
                            contentColor = colorResource(id = R.color.heavy_metal),
                        )
                    ) {
                        Text(
                            text = stringResource(id =
                                R.string.main_activity_accept_permission_granting),
                        )
                    }
                },
            )
        }
    }


    @Composable
    fun ScreenLayout() {
        val context = LocalContext.current
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(250.dp),
                    drawerContainerColor =
                        colorResource(id = R.color.black_and_brown),
                ) {
                    DrawerHeader()
                    DrawerBody()
                }
            },
        ) {
            Scaffold(
                containerColor = colorResource(id = R.color.main_bg),
                topBar = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(
                                    start = 8.dp,
                                    top = 8.dp
                                ),
                            onClick = {
                                scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            } }) {
                            Icon(
                                Icons.Default.Menu,
                                modifier = Modifier.size(90.dp),
                                contentDescription = "Profile",
                                tint = colorResource(id = R.color.grey_neutral)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(
                                    end = 8.dp,
                                    top = 8.dp
                                ),
                            onClick = {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                            }) {
                            Icon(
                                Icons.Default.AccountCircle,
                                modifier = Modifier.size(90.dp),
                                contentDescription = "Profile",
                                tint = colorResource(id = R.color.grey_neutral)
                            )
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        vm.startOpenNoteActivity(context)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp)) {
                        HorizontalDivider()
                        NotesList()
                    }
                }
            }
        }

        val toastNotification by vm.toastNotification.collectAsState()

        if (toastNotification.isNotEmpty()) {
            LaunchedEffect(vm.toastNotification) {
                Toast.makeText(context, toastNotification,
                    Toast.LENGTH_SHORT).show()
                vm.clearToastNotification()
            }
        }
    }

    @Composable
    fun NotesList() {
        val noteBoxesList by vm.noteBoxesList.collectAsState()

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(noteBoxesList) { item ->
                val isVisible = item.visible.collectAsState()
                Log.d("MainView", isVisible.toString())
                AnimatedVisibility(
                    visible = isVisible.value,
                    enter = slideInHorizontally()
                            + expandHorizontally(expandFrom = Alignment.End)
                            + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
                            + shrinkHorizontally()
                            + fadeOut(),
                ) {
                    NoteBox(
                        noteBoxModel = item,
                        viewModel = vm,
                    )
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        vm.refreshNotesList()
    }
}



