package com.kelnik.htracker.ui.common

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.kelnik.htracker.ui.theme.AppTheme

@Composable
fun MainEntry() {
    var theme by rememberSaveable {
        mutableStateOf(AppTheme.Theme.Light)
    }
    AppTheme(theme) {
        AppScaffold { theme = it }
    }
}