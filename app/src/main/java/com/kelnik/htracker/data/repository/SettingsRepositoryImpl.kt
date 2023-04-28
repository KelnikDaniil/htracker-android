package com.kelnik.htracker.data.repository

import com.kelnik.htracker.data.preferences.SharedPreferencesSettingsStorage
import com.kelnik.htracker.domain.entity.Language
import com.kelnik.htracker.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferencesSettingsStorage: SharedPreferencesSettingsStorage
) : SettingsRepository {
    override var isDarkTheme: Boolean
        get() = sharedPreferencesSettingsStorage.getIsDarkTheme()
        set(value) = sharedPreferencesSettingsStorage.setIsDarkTheme(value)
    override var language: Language
        get() = sharedPreferencesSettingsStorage.getLanguage()
        set(value) = sharedPreferencesSettingsStorage.setLanguage(value)
}