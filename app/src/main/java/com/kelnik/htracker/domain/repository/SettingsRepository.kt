package com.kelnik.htracker.domain.repository

import com.kelnik.htracker.domain.entity.Language

interface SettingsRepository {
    var isDarkTheme: Boolean
    var language: Language
}
