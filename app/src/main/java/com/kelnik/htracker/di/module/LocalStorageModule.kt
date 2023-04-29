package com.kelnik.htracker.di.module

import android.content.Context
import androidx.room.Room
import com.kelnik.htracker.data.local.AppDatabase
import com.kelnik.htracker.data.local.dao.EventNotificationDao
import com.kelnik.htracker.data.local.dao.HabitDao
import com.kelnik.htracker.data.local.model.EventNotificationDbModel
import com.kelnik.htracker.data.local.model.HabitDbModel
import com.kelnik.htracker.data.mapper.EventNotificationMapperImpl
import com.kelnik.htracker.data.mapper.HabitMapperImpl
import com.kelnik.htracker.domain.entity.EventNotification
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface LocalStorageModule {
    @Binds
    fun provideMapperHabit(habitMapperImpl: HabitMapperImpl ): Mapper<Habit, HabitDbModel>

    @Binds
    fun provideMapperEventNotification(eventNotificationMapperImpl: EventNotificationMapperImpl): Mapper<EventNotification, EventNotificationDbModel>


    companion object {
        @Singleton
        @Provides
        fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }

        @Singleton
        @Provides
        fun provideHabitDao(appDatabase: AppDatabase): HabitDao = appDatabase.habitDao()

        @Singleton
        @Provides
        fun provideEventNotificationDao(appDatabase: AppDatabase): EventNotificationDao = appDatabase.eventNotificationDao()
    }
}