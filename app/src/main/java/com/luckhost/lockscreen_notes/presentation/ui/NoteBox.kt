package com.luckhost.lockscreen_notes.presentation.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.yourapp.ui.components.ConfirmDialog
import com.luckhost.lockscreen_notes.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun NoteBox(
    title: String,
    content: String,
    bitmap: ImageBitmap?,
    onItemClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp)
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
                    tint = colorResource(id = R.color.notebox_title_text)
                )
            }

            if (showDialog) {
                ConfirmDialog(
                    text = "Вы хотите удалить заметку?",
                    onConfirm = {
                        onDeleteIconClick()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 4.dp)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                maxLines = 1,
                style = TextStyle(
                    color = colorResource(id = R.color.notebox_title_text),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            HorizontalDivider(
                modifier = Modifier.constrainAs(dividerRef) {
                    top.linkTo(titleRef.bottom, margin = 4.dp)
                },
                thickness = 1.dp,
                color = colorResource(id = R.color.grey_neutral)
            )

            MarkdownText(
                modifier = Modifier.constrainAs(contentRef) {
                    top.linkTo(dividerRef.bottom, margin = 4.dp)
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 4.dp)
                    bitmap?.let {
                        end.linkTo(imageRef.start, margin = 8.dp)
                    } ?: end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },
                markdown = content.trimIndent(),
                maxLines = 5,
                style = TextStyle(
                    color = colorResource(id = R.color.grey_neutral),
                    fontSize = 16.sp
                ),
                onClick = { onItemClick() }
            )

            if (bitmap != null) {
                Image(
                    modifier = Modifier
                        .constrainAs(imageRef) {
                            top.linkTo(dividerRef.bottom, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 6.dp)
                            end.linkTo(parent.end)
                        }
                        .size(70.dp),
                    bitmap = bitmap,
                    contentDescription = title,
                    alignment = Alignment.TopEnd,
                )
            }
        }
    }
}
