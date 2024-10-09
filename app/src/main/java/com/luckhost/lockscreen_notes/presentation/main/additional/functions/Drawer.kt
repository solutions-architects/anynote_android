package com.luckhost.lockscreen_notes.presentation.main.additional.functions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R

/**
 *
 */

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp),
            style = TextStyle(
                color = colorResource(id = R.color.main_title_text),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Start
            ),
        )
    }
    HorizontalDivider(thickness = 2.dp, color = colorResource(id = R.color.stroke_color))
}

@Composable
fun DrawerBody() {

    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
    ) {
        DrawerButton(
            icon = Icons.Default.Settings,
            text = stringResource(id = R.string.drawer_settings_button),
            onClick = {}
        )
        DrawerButton(
            icon = Icons.Default.Info,
            text = stringResource(id = R.string.drawer_about_button),
            onClick = {

            }
        )
    }
}

@Composable
fun DrawerButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(
                top = 5.dp,
                bottom = 5.dp
            )
            .fillMaxWidth(),

        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.black_and_brown),
            contentColor = colorResource(id = R.color.grey_neutral),
        ),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                modifier = Modifier.size(28.dp),
                contentDescription = "Profile",
                tint = colorResource(id = R.color.grey_neutral)
            )
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = text,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Start
                ),
            )
        }
    }
}