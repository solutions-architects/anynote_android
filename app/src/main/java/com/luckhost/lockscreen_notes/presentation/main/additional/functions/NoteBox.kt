package com.luckhost.lockscreen_notes.presentation.main.additional.functions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun NoteBox(
    content: List<Map<String, String>>,
    onItemClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(color = colorResource(R.color.notebox_bg))
            .wrapContentHeight()
            .clickable {
                onItemClick()
            },
        contentAlignment = Alignment.Center,
        
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(15.dp)
        ) {
            val (titleRef, contentRef, imageRef, dividerRef, buttonRef) = createRefs()

            var showDialog by remember { mutableStateOf(false) }

            IconButton(onClick = { showDialog = true },
                modifier = Modifier
                    .constrainAs(buttonRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .size(30.dp)) {
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
                        onDeleteIconClick()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }


            /*TODO*/ // слишком громостко и долго
            for (entry in content) {
                when(entry["name"]) {
                    "info" -> {
                        entry["header"]?.let {
                            TitlePart(modifier = Modifier
                                .constrainAs(titleRef) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start, margin = 4.dp)
                                    end.linkTo(parent.end)
                                }
                                .fillMaxWidth(), title = it)
                            HorizontalDivider(
                                modifier = Modifier.constrainAs(dividerRef) {
                                    top.linkTo(titleRef.bottom, margin = 4.dp)
                                },
                                thickness = 1.dp,
                                color = colorResource(id = R.color.grey_neutral)
                            )
                        }
                    }
                    "md" -> {
                        entry["text"]?.let {
                            MarkdownPart(
                                modifier = Modifier.constrainAs(contentRef) {
                                    top.linkTo(dividerRef.bottom, margin = 4.dp)
                                    bottom.linkTo(parent.bottom, margin = 4.dp)
                                    start.linkTo(parent.start, margin = 4.dp)
                                    width = Dimension.fillToConstraints },
                                string = it,
                                onItemClick = onItemClick,
                            )
                        }
                    }
                    "map" -> { }
                }
            }

        }
    }
}

@Composable
private fun MarkdownPart(
    modifier: Modifier,
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
private fun TitlePart(
    modifier: Modifier,
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