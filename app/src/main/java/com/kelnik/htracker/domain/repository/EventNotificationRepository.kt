package com.kelnik.htracker.domain.repository

import com.kelnik.htracker.domain.entity.EventNotification
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventNotificationRepository {
    suspend fun addEventNotification(eventNotification: EventNotification): Resource<Unit>
    suspend fun doneEventNotification(eventNotificationId: Int): Resource<Unit>
    suspend fun cancelEventNotification(eventNotificationId: Int): Resource<Unit>
    suspend fun removeEventNotification(eventNotificationId: Int): Resource<Unit>
    suspend fun removeEventNotificationForHabit(habitId: Int): Resource<Unit>
    suspend fun getEventNotification(eventNotificationId: Int): Resource<EventNotification>
    suspend fun getEventNotificationListForHabit(habitId: Int): Resource<List<EventNotification>>


    suspend fun addEventNotificationsLaterThanDateInclusiveForHabit(habit: Habit, date: LocalDate): Resource<List<EventNotification>>
    suspend fun removeEventNotificationsLaterThanDateInclusiveWhereIsDoneFalseForHabit(habitId: Int, date: LocalDate): Resource<List<EventNotification>>
    suspend fun toggleIsDoneEventNotification(eventNotification: EventNotification): Resource<Boolean>
    suspend fun getEventNotificationList(): Resource<Flow<List<EventNotification>>>
}