package com.luckhost.lockscreen_notes.presentation.main.additional.functions

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.luckhost.lockscreen_notes.presentation.ui.ConfirmDialog
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.main.MainViewModel
import com.luckhost.lockscreen_notes.presentation.openNote.additional.models.NoteBoxModel
import dev.jeziellago.compose.markdowntext.MarkdownText

@Stable
@Composable
fun NoteBox(
    noteBoxModel: NoteBoxModel,
    viewModel: MainViewModel,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                viewModel.startOpenNoteActivity(
                    context = context,
                    noteBoxModel.parentHash
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.notebox_bg)
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 15.dp,
            pressedElevation = 0.dp
        ),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(15.dp)
        ) {
            val (titleRef, contentRef, dividerRef, buttonRef) = createRefs()

            var showDialog by remember { mutableStateOf(false) }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .constrainAs(buttonRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = colorResource(id = R.color.main_title_text)
                )
            }

            if (showDialog) {
                ConfirmDialog(
                    text = stringResource(id = R.string.question_of_note_deleting),
                    onConfirm = {
                        viewModel.deleteNote(noteBoxModel.parentHash)
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }

            HorizontalDivider(
                modifier = Modifier.constrainAs(dividerRef) {
                    top.linkTo(titleRef.bottom, margin = 4.dp)
                },
                thickness = 1.dp,
                color = colorResource(id = R.color.grey_neutral)
            )

            TitlePart(
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 4.dp)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                title = noteBoxModel.title
            )

            Row(
                modifier = Modifier.constrainAs(contentRef) {
                    top.linkTo(dividerRef.bottom, margin = 4.dp)
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 4.dp)
                    width = Dimension.fillToConstraints
                }.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MarkdownPart(
                    modifier = Modifier.weight(1f),
                    string = noteBoxModel.mdText,
                    onItemClick = { viewModel.startOpenNoteActivity(context = context,
                        noteBoxModel.parentHash) },
                )

                noteBoxModel.imageSource?.let {
                    ImagePart(
                        imageLocalStorageLink = it
                    )
                }
            }
        }
    }
}

@Composable
private fun TitlePart(
    modifier: Modifier = Modifier,
    title: String,) {
    Text(
        text = title,
        modifier = modifier,
        maxLines = 1,
        style = TextStyle(
            color = colorResource(id = R.color.main_title_text),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        ),
    )
}

@Composable
private fun MarkdownPart(
    modifier: Modifier = Modifier,
    string: String,
    onItemClick: () -> Unit
) {
    MarkdownText(
        modifier = modifier,
        markdown = string.trimIndent(),
        maxLines = 5,
        style = TextStyle(
            color = colorResource(id = R.color.grey_neutral),
            fontSize = 16.sp
        ),
        onClick = { onItemClick() }
    )
}

@Composable
private fun ImagePart(
    modifier: Modifier = Modifier,
    imageLocalStorageLink: String,) {

    val bitmap = BitmapFactory.decodeFile(imageLocalStorageLink)

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            alignment = Alignment.CenterEnd,
            contentDescription = "Round corner image",
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .size(84.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(10))
                .border(1.dp, Color.Gray, RoundedCornerShape(10))
        )
    }
}

