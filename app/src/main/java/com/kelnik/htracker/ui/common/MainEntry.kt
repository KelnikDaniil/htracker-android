package com.kelnik.htracker.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kelnik.htracker.domain.entity.Language
import com.kelnik.htracker.ui.page.settings.SettingsViewModel
import com.kelnik.htracker.ui.theme.AppTheme
import com.kelnik.htracker.utils.LocaleUtils

@Composable
fun MainEntry(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    var theme by rememberSaveable {
        mutableStateOf(
            when (settingsViewModel.viewStates.isDarkTheme.also { println(">>>>>>>>>>>>>>>>>>>>> isDarkTheme=$it") }) {
                true -> AppTheme.Theme.Dark
                false -> AppTheme.Theme.Light
            }
        )
    }

    var lang by rememberSaveable {
        mutableStateOf(
            when (settingsViewModel.viewStates.language) {
                Language.RUSSIAN -> "ru"
                Language.ENGLISH -> "en"
            }
        )
    }
    LocaleUtils.setLocale(LocalContext.current, lang)
    AppTheme(theme) {
        AppScaffold(
            onThemeChange = { theme = it },
            onLanguageChange = { lang = when(it){
                Language.RUSSIAN -> "ru"
                Language.ENGLISH -> "en"
            } },
            lang = lang
        )
    }
}
