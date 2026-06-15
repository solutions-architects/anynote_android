package com.luckhost.lockscreen_notes.presentation.screens.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckhost.lockscreen_notes.R
import com.luckhost.lockscreen_notes.presentation.screens.userLogin.ui.theme.Lockscreen_notesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class SettingsActivity : AppCompatActivity() {

    private val vm by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lockscreen_notesTheme {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun SettingsScreen() {
        val isDark by vm.isDarkTheme.collectAsState()
        val columns by vm.columnsCount.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.main_bg))
                .padding(16.dp)
        ) {
            Text(
                text = "Settings",
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 2.dp,
                color = colorResource(id = R.color.stroke_color)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark theme",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = colorResource(id = R.color.main_title_text),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                )
                Switch(
                    checked = isDark,
                    onCheckedChange = { vm.toggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.main_bg),
                        checkedTrackColor = colorResource(id = R.color.grey_neutral),
                        uncheckedThumbColor = colorResource(id = R.color.grey_neutral),
                        uncheckedTrackColor = colorResource(id = R.color.light_grey),
                        uncheckedBorderColor = colorResource(id = R.color.stroke_color),
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = colorResource(id = R.color.stroke_color)
            )

            Text(
                text = "Columns: $columns",
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = columns.toFloat(),
                onValueChange = { vm.changeColumns(it.roundToInt()) },
                valueRange = 1f..3f,
                steps = 1,
                colors = SliderDefaults.colors(
                    thumbColor = colorResource(id = R.color.grey_neutral),
                    activeTrackColor = colorResource(id = R.color.grey_neutral),
                    inactiveTrackColor = colorResource(id = R.color.stroke_color),
                    activeTickColor = colorResource(id = R.color.main_bg),
                    inactiveTickColor = colorResource(id = R.color.grey_neutral),
                )
            )
        }
    }
}
