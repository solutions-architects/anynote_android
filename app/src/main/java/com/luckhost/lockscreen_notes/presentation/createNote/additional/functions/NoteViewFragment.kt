package com.luckhost.lockscreen_notes.presentation.createNote.additional.functions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.domain.models.NoteModel
import com.luckhost.lockscreen_notes.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun NoteViewFragment(note: NoteModel) {
    Column {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)
                .fillMaxWidth(),
            text = note.header,
            style = TextStyle(
                color = colorResource(id = R.color.main_title_text),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            ),
        )
        HorizontalDivider()
        MarkdownText(modifier = Modifier.padding(8.dp),
            markdown = note.content.trimIndent(),
            style = TextStyle(
                color = colorResource(id = R.color.grey_neutral),
                fontSize = 24.sp
            ),
        )
    }
}