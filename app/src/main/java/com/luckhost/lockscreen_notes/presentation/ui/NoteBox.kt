package com.luckhost.lockscreen_notes.presentation.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.luckhost.lockscreen_notes.R

@Composable
fun NoteBox(
    title: String,
    content: String,
    bitmap: ImageBitmap,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = colorResource(R.color.grey_neutral))
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                color = colorResource(id = R.color.black_and_brown)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = content,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    bitmap = bitmap,
                    contentDescription = title,
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}


@Composable
fun NoteBox(
    context: Context,
    title: String,
    content: String,
    soundId:Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Red)
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(title)
            Row {
                Text(text = content)
                Button(onClick = {
                    val mediaPlayer = MediaPlayer.create(context, soundId)
                    mediaPlayer.start()
                }) {

                }
            }
        }
    }
}