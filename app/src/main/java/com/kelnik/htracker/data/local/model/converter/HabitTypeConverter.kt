package com.kelnik.htracker.data.local.model.converter

import androidx.room.TypeConverter
import com.kelnik.htracker.domain.entity.Habit.Companion.HabitType

object HabitTypeConverter {
    @TypeConverter
    fun toHabitType(value: String): HabitType {
        return HabitType.valueOf(value)
    }

    @TypeConverter
    fun fromHabitType(habitType: HabitType): String {
        return habitType.name
    }
}