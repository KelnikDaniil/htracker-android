package com.kelnik.htracker.domain.repository

import com.kelnik.htracker.domain.entity.EventNotification
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventNotificationSchedulerRepository {
    suspend fun scheduleNotificationEvent(eventNotification: EventNotification): Resource<Unit>
    suspend fun scheduleNotificationEvent(eventNotifications: List<EventNotification>): Resource<Unit>
    suspend fun cancelNotificationEvent(eventNotification: EventNotification): Resource<Unit>
    suspend fun cancelNotificationEvent(eventNotifications: List<EventNotification>): Resource<Unit>
}