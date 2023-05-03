package com.kelnik.htracker.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kelnik.htracker.domain.interactor.EventNotificationSchedulerUseCase
import com.kelnik.htracker.domain.interactor.EventNotificationUseCase
import com.kelnik.htracker.utils.checkFailure
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootCompleteReceiver : BroadcastReceiver() {
    @Inject
    lateinit var eventNotificationUseCase: EventNotificationUseCase

    @Inject
    lateinit var eventNotificationSchedulerUseCase: EventNotificationSchedulerUseCase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            CoroutineScope(Dispatchers.IO).launch {
                val eventNotifications =
                    eventNotificationUseCase.getEventNotificationList().checkFailure()
                val eventNotificationList = eventNotifications.last()
                    .filter { it.date.isAfter(LocalDateTime.now()) && !it.isDone }
                eventNotificationSchedulerUseCase.scheduleNotificationEvent(eventNotificationList)
            }
        }
    }
}