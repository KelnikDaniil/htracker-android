package com.kelnik.htracker.di.module

import com.kelnik.htracker.data.repository.EventNotificationRepositoryImpl
import com.kelnik.htracker.data.repository.HabitRepositoryImpl
import com.kelnik.htracker.domain.repository.EventNotificationRepository
import com.kelnik.htracker.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun bindHabitRepository(habitRepositoryImpl: HabitRepositoryImpl): HabitRepository

    @Binds
    fun bindEventNotificationRepository(eventNotificationRepositoryImpl: EventNotificationRepositoryImpl): EventNotificationRepository
}