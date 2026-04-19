package com.luckhost.lockscreen_notes.presentation.screens.settings

import androidx.lifecycle.ViewModel
import com.luckhost.domain.useCases.settings.GetColumnsCountUseCase
import com.luckhost.domain.useCases.settings.SetColumnsCountUseCase
import com.luckhost.domain.useCases.theme.GetThemeStateUseCase
import com.luckhost.domain.useCases.theme.ToggleThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val getThemeStateUseCase: GetThemeStateUseCase,
    private val toggleThemeUseCase: ToggleThemeUseCase,

    private val getColumnsCountUseCase: GetColumnsCountUseCase,
    private val setColumnsCountUseCase: SetColumnsCountUseCase
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(getThemeStateUseCase())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _columnsCount = MutableStateFlow(getColumnsCountUseCase())
    val columnsCount: StateFlow<Int> = _columnsCount

    fun toggleTheme() {
        toggleThemeUseCase()
        _isDarkTheme.value = getThemeStateUseCase()
    }

    fun changeColumns(count: Int) {
        _columnsCount.value = count
        setColumnsCountUseCase(count)
    }
}
