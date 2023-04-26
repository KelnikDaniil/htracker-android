package com.kelnik.htracker.data.local.model.converter

import androidx.room.TypeConverter
import com.kelnik.htracker.domain.entity.Habit.Companion.RepeatType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalTimeTypeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun toLocalTime(value: String): LocalTime {
        return formatter.parse(value, LocalTime::from)
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.format(formatter)
    }
}