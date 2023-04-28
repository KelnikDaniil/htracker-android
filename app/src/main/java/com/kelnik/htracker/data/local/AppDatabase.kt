package com.kelnik.htracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kelnik.htracker.data.local.dao.EventNotificationDao
import com.kelnik.htracker.data.local.dao.HabitDao
import com.kelnik.htracker.data.local.model.EventNotificationDbModel
import com.kelnik.htracker.data.local.model.HabitDbModel
import com.kelnik.htracker.data.local.model.converter.*

@Database(entities = [HabitDbModel::class, EventNotificationDbModel::class], version = 1)
@TypeConverters(
    HabitTypeConverter::class,
    LocalDateTimeTypeConverter::class,
    LocalDateTypeConverter::class,
    LocalTimeTypeConverter::class,
    RepeatTypeConverter::class,
    SetDayTypeConverter::class,
    TargetTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun eventNotificationDao(): EventNotificationDao

    companion object {
        const val DATABASE_NAME = "habit-tracker-database"
    }
}