package com.kelnik.htracker.ui.common

import androidx.annotation.StringRes
import com.kelnik.htracker.R

sealed class BottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    var iconId: Int
) {
    object Today :
        BottomNavRoute(RouteName.TODAY, R.string.today, R.drawable.ic_today)

    object Habits :
        BottomNavRoute(RouteName.HABITS, R.string.habits, R.drawable.ic_habits)

    object History :
        BottomNavRoute(RouteName.HISTORY, R.string.history, R.drawable.ic_history)
}