package com.kelnik.htracker.data.local.model.converter

import androidx.room.TypeConverter
import com.kelnik.htracker.domain.entity.Habit.Companion.TargetType

object TargetTypeConverter {
    @TypeConverter
    fun toTargetType(value: String): TargetType {
        return TargetType.valueOf(value)
    }

    @TypeConverter
    fun fromTargetType(targetType: TargetType): String {
        return targetType.name
    }
}