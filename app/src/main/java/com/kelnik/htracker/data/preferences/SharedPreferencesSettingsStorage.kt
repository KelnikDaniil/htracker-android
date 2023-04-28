package com.kelnik.htracker.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.kelnik.htracker.domain.entity.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class SharedPreferencesSettingsStorage @Inject constructor(@ApplicationContext context: Context) {
    private val settingsPref: SharedPreferences

    init {
        settingsPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getIsDarkTheme(): Boolean {
        return settingsPref.getBoolean(PREF_KEY_IS_DARK_THEME, false).also {
            println("+++++++++++++ prefrences getIsDarkTheme=${it}")
        }
    }

    fun setIsDarkTheme(value: Boolean) {
        println("++++++++++++++ prefrences setIsDarkTheme=${value}")
        settingsPref.edit().putBoolean(PREF_KEY_IS_DARK_THEME, value).apply()
    }

    fun getLanguage(): Language {
        val value = settingsPref.getString(PREF_KEY_LANGUAGE, Language.RUSSIAN.toString())
            ?: Language.RUSSIAN.toString()
        return Language.valueOf(value)
    }

    fun setLanguage(language: Language) {
        settingsPref.edit().putString(PREF_KEY_LANGUAGE, language.toString()).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.kelnik.htracker"
        private const val PREF_KEY_IS_DARK_THEME = "is_dark_theme"
        private const val PREF_KEY_LANGUAGE = "language"

    }
}