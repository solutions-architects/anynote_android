package com.luckhost.lockscreen_notes.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.main.additional.functions.NoteBox
import com.luckhost.lockscreen_notes.presentation.userLogin.LoginActivity
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lockscreen_notesTheme {
                ScreenLayout()
            }
        }
    }

    @Composable
    fun ScreenLayout() {
        val context = LocalContext.current

        Scaffold(
            containerColor = colorResource(id = R.color.main_bg),
            topBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        modifier = Modifier
                            .padding(start = 8.dp,
                                top = 8.dp),
                        onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Menu,
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
                        Icon(Icons.Default.AccountCircle,
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
            }
        ) {
            innerPadding  ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                Column(modifier = Modifier.padding(top = 15.dp)) {
                    HorizontalDivider()
                    NotesList()
                }
            }
        }
    }

    @Composable
    fun NotesList() {
        val noteBoxesList by vm.noteBoxesList.collectAsState()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(noteBoxesList) { item ->
                NoteBox(
                    noteBoxModel = item,
                    viewModel = vm,
                )
            }
        }

    }

    override fun onResume() {
        super.onResume()
        vm.refreshNotesList()
    }
}



