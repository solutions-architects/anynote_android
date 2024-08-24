package com.luckhost.lockscreen_notes.presentation.openNote

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.openNote.additional.functions.EditNoteFragment
import com.luckhost.lockscreen_notes.presentation.openNote.additional.functions.NoteViewFragment
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenNoteActivity : ComponentActivity() {
    private val vm by viewModel<OpenNoteViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setDecorFitsSystemWindows(false)

        val extras = intent.extras
        
        extras?.let {
            val noteHash = intent.getIntExtra("noteHash", 0)
            vm.getNote(noteHash)
        }?: run {
            vm.createEmptyNote()
        }

        setContent {
            Lockscreen_notesTheme {
                val isEditMode by vm.isEditMode.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = colorResource(id = R.color.main_bg),
                    floatingActionButton = {
                        Column {
                            SaveFloatingButton(isEditMode)
                            AttachButton(isEditMode)
                        }
                        EnterEditModeButton(!isEditMode)
                    }
                ) {
                    innerPadding  ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        AnimatedContent(
                            targetState = isEditMode,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith
                                        fadeOut(animationSpec = tween(300))
                            }, label = ""
                        ) { targetState ->
                            if (targetState) {
                                EditNoteFragment(vm = vm)
                            } else {
                                NoteViewFragment(vm = vm)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SaveFloatingButton(shouldDisplay: Boolean) {
        AnimatedVisibility(
            visible = shouldDisplay,
            enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter),
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.TopCenter)
        ) {
            FloatingActionButton(modifier = Modifier
                .padding(10.dp)
                .size(65.dp),
                onClick = { vm.saveChanges()
                          vm.changeEditModeState() },
                containerColor =
                    colorResource(id = R.color.heavy_metal),
                contentColor =
                    colorResource(id = R.color.grey_neutral)
            ) {
                Icon(Icons.Default.Check, contentDescription = "save")
            }
        }
    }

    @Composable
    private fun AttachButton(shouldDisplay: Boolean) {

        val context = LocalContext.current
        val photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) {
            if (it != null) {
                val realPath = vm.getRealPathFromUri(context, it)
                if (realPath != null) {
                    vm.changePasteTextState(realPath)
                }
                Log.d("PhotoPicker", "Selected path: ${realPath}")
            }
        }

        AnimatedVisibility(
            visible = shouldDisplay,
            enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter),
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.TopCenter)
        ) {
            FloatingActionButton(modifier = Modifier
                .padding(10.dp)
                .size(65.dp),
                onClick = { photoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                ) },
                containerColor =
                colorResource(id = R.color.heavy_metal),
                contentColor =
                colorResource(id = R.color.grey_neutral)
            ) {
                Icon(painterResource(id = R.drawable.attach_file), contentDescription = "save")
            }
        }
    }

    @Composable
    private fun EnterEditModeButton(shouldNotDisplay: Boolean) {
        AnimatedVisibility(
            visible = shouldNotDisplay,
            enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter),
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.TopCenter)
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(10.dp)
                    .size(65.dp),
                onClick = { vm.changeEditModeState() },
                containerColor =
                colorResource(id = R.color.heavy_metal),
                contentColor =
                colorResource(id = R.color.grey_neutral)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}