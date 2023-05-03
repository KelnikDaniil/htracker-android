package com.kelnik.htracker.domain.interactor

import com.kelnik.htracker.domain.entity.EventNotification
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.repository.EventNotificationRepository
import com.kelnik.htracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class EventNotificationUseCase @Inject constructor(
    private val eventNotificationRepository: EventNotificationRepository
) {
    suspend fun loadEventNotificationsForHabit(
        habit: Habit,
        onCancelNotifications: (List<EventNotification>) -> Unit,
        onInitNotifications: (List<EventNotification>) -> Unit
    ): Resource<Unit> {
        val currentDate = LocalDate.now()
        val deletedNotifications = eventNotificationRepository
            .removeEventNotificationsLaterThanDateInclusiveWhereIsDoneFalseForHabit(
                habit.id,
                currentDate
            )
        when (deletedNotifications) {
            is Resource.Failure -> return Resource.Failure(deletedNotifications.throwable)
            is Resource.Success -> onCancelNotifications(deletedNotifications.data)
        }

        val newNotifications = eventNotificationRepository
            .addEventNotificationsLaterThanDateInclusiveForHabit(habit, currentDate)
        when (newNotifications) {
            is Resource.Failure -> return Resource.Failure(newNotifications.throwable)
            is Resource.Success -> onInitNotifications(newNotifications.data)
        }
        return Resource.Success(Unit)
    }

    suspend fun removeEventNotificationsForHabit(
        habitId: Int,
        onCancelNotifications: (List<EventNotification>) -> Unit,
    ): Resource<Unit> {
        val deletedEventNotifications =
            eventNotificationRepository.getEventNotificationListForHabit(habitId)
        when (deletedEventNotifications) {
            is Resource.Failure -> return Resource.Failure(deletedEventNotifications.throwable)
            is Resource.Success -> {
                val result = eventNotificationRepository.removeEventNotificationForHabit(habitId)
                when (result) {
                    is Resource.Failure -> return Resource.Failure(result.throwable)
                    is Resource.Success -> {
                        onCancelNotifications(deletedEventNotifications.data)
                        return Resource.Success(Unit)
                    }
                }
            }
        }
    }


    //    suspend fun addEventNotification(eventNotification: EventNotification): Resource<Unit> =
//        eventNotificationRepository.addEventNotification(eventNotification)
//
//    suspend fun doneEventNotification(eventNotificationId: Int): Resource<Unit> =
//        eventNotificationRepository.doneEventNotification(eventNotificationId)
//
    suspend fun toggleIsDoneEventNotification(
        eventNotification: EventNotification,
        onCancelNotification: (EventNotification) -> Unit,
        onInitNotification: (EventNotification) -> Unit
    ): Resource<Unit> {
        val isDoneResult =
            eventNotificationRepository.toggleIsDoneEventNotification(eventNotification)
        when (isDoneResult) {
            is Resource.Failure -> return Resource.Failure(isDoneResult.throwable)
            is Resource.Success -> {
                when (isDoneResult.data) {
                    true -> onInitNotification(eventNotification)
                    false -> onCancelNotification(eventNotification)
                }
            }
        }
        return Resource.Success(Unit)
    }


    //
//    suspend fun cancelEventNotification(eventNotificationId: Int): Resource<Unit> =
//        eventNotificationRepository.cancelEventNotification(eventNotificationId)
//
//    suspend fun removeEventNotification(eventNotificationId: Int): Resource<Unit> =
//        eventNotificationRepository.removeEventNotification(eventNotificationId)
//
//    suspend fun removeEventNotificationForHabit(habitId: Int): Resource<Unit> =
//        eventNotificationRepository.removeEventNotificationForHabit(habitId)
//
    suspend fun getEventNotification(eventNotificationId: Int): Resource<EventNotification> =
        eventNotificationRepository.getEventNotification(eventNotificationId)

//    suspend fun getEventNotificationListForHabit(habitId: Int): Resource<List<EventNotification>> =
//        eventNotificationRepository.getEventNotificationListForHabit(habitId)

    //
    suspend fun getEventNotificationList(): Resource<Flow<List<EventNotification>>> =
        eventNotificationRepository.getEventNotificationList()

}