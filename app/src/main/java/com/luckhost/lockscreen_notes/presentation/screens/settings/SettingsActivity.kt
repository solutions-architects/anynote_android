package com.luckhost.lockscreen_notes.presentation.screens.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
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
                Surface {
                    SettingsScreen()
                }
            }
        }
    }

    @Composable
    fun SettingsScreen() {

        val isDark by vm.isDarkTheme.collectAsState()
        val columns by vm.columnsCount.collectAsState()
        val language by vm.language.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                "Settings",
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Dark Theme",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = colorResource(id = R.color.main_title_text),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    ),
                )

                Switch(
                    checked = isDark,
                    onCheckedChange = {
                        vm.toggleTheme()
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "Columns count: $columns",
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            Slider(
                value = columns.toFloat(),
                onValueChange = {
                    vm.changeColumns(it.roundToInt())
                },
                valueRange = 1f..3f,
                steps = 1 // 1-2-3
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                stringResource(R.string.settings_language_label),
                style = TextStyle(
                    color = colorResource(id = R.color.main_title_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            listOf("en" to R.string.settings_language_en, "ru" to R.string.settings_language_ru)
                .forEach { (code, labelRes) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = language == code,
                            onClick = {
                                vm.setLanguage(code)
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags(code)
                                )
                            }
                        )
                        Text(
                            text = stringResource(labelRes),
                            style = TextStyle(
                                color = colorResource(id = R.color.main_title_text),
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                        )
                    }
                }
        }
    }
}
