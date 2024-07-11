package com.luckhost.lockscreen_notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.ui.NoteBox
import com.luckhost.lockscreen_notes.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val vm by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lockscreen_notesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.heavy_metal)
                ) {
                    Column {
                        NoteObj()
                        NoteObj()
                    }

                }
            }
        }
    }
}

@Composable
fun NoteObj() {
    NoteBox(title = "test",
        content = "lorem ipsum",
        bitmap = ImageBitmap.imageResource(R.drawable.aboba)) {
    }
}
