package com.kelnik.htracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kelnik.htracker.data.local.dao.HabitDao
import com.kelnik.htracker.data.local.model.HabitDbModel

@Database(entities = [HabitDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun eventNotificationDao(): HabitDao

    companion object {
        const val DATABASE_NAME = "habit-tracker-database"
    }
}