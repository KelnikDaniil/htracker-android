package com.kelnik.htracker.domain.interactor

import com.kelnik.htracker.domain.entity.EventNotification
import com.kelnik.htracker.domain.repository.EventNotificationRepository
import com.kelnik.htracker.utils.Resource
import javax.inject.Inject

class EventNotificationUseCase @Inject constructor(
    private val eventNotificationRepository: EventNotificationRepository
) {
    suspend fun addEventNotification(eventNotification: EventNotification): Resource<Unit> =
        eventNotificationRepository.addEventNotification(eventNotification)

    suspend fun doneEventNotification(eventNotificationId: Int): Resource<Unit> =
        eventNotificationRepository.doneEventNotification(eventNotificationId)

    suspend fun cancelEventNotification(eventNotificationId: Int): Resource<Unit> =
        eventNotificationRepository.cancelEventNotification(eventNotificationId)

    suspend fun removeEventNotification(eventNotificationId: Int): Resource<Unit> =
        eventNotificationRepository.removeEventNotification(eventNotificationId)

    suspend fun removeEventNotificationForHabit(habitId: Int): Resource<Unit> =
        eventNotificationRepository.removeEventNotificationForHabit(habitId)

    suspend fun getEventNotification(eventNotificationId: Int): Resource<EventNotification> =
        eventNotificationRepository.getEventNotification(eventNotificationId)

    suspend fun getEventNotificationListForHabit(habitId: Int): Resource<List<EventNotification>> =
        eventNotificationRepository.getEventNotificationListForHabit(habitId)

    suspend fun getEventNotificationList(): Resource<List<EventNotification>> =
        eventNotificationRepository.getEventNotificationList()
}